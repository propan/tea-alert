(ns tea-alert.buffer
  (:refer-clojure :exclude [peek])
  (:require [com.stuartsierra.component :as component]
            [clojure.java.io :as io]))

;; TODO: remove when official implementation is available https://dev.clojure.org/jira/browse/CLJ-1454
(defn- get-and-reset!
  [atm new-value]
  (let [old-value @atm]
    (if (compare-and-set! atm old-value new-value)
      (do
        (.notifyWatches atm old-value new-value)
        old-value)
      (recur atm new-value))))

(defrecord S3Buffer [data]
  component/Lifecycle

  (start [component]
    (assoc component :data (atom {})))

  (stop [{:keys [data] :as component}]
    (assoc component :data nil)))


(defn create-buffer
  []
  (map->S3Buffer {}))

(defn put!
  [{:keys [data]} {:keys [name items]}]
  (swap! data update-in [name] #(concat % items)))

(defn peek
  [{:keys [data]}]
  (deref data))

(defn take!
  [{:keys [data]}]
  (get-and-reset! data {}))
