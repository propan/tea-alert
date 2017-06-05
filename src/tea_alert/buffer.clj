(ns tea-alert.buffer
  (:refer-clojure :exclude [peek])
  (:require [com.stuartsierra.component :as component]
            [tea-alert.drivers :as d]))

;; TODO: remove when official implementation is available https://dev.clojure.org/jira/browse/CLJ-1454
(defn- get-and-reset!
  [atm new-value]
  (let [old-value @atm]
    (if (compare-and-set! atm old-value new-value)
      (do
        (.notifyWatches atm old-value new-value)
        old-value)
      (recur atm new-value))))

(defrecord S3Buffer [data driver]
  component/Lifecycle

  (start [component]
    (let [data (atom (if (nil? driver) {} (d/load driver)))]
      (when-not (nil? driver)
        (add-watch data :s3-uploader #(d/save driver %4)))
      (assoc component :data data)))

  (stop [{:keys [data] :as component}]
    (remove-watch data :s3-uploader)
    (assoc component :data nil)))

(defn create-buffer
  [driver]
  (map->S3Buffer {:driver driver}))

(defn put!
  [{:keys [data]} items]
  (swap! data #(reduce (fn [a {:keys [name items]}]
                         (update a name concat items)) % items)))

(defn peek
  [{:keys [data]}]
  (mapv (fn [[k v]] {:name k :items v}) (seq @data)))

(defn take!
  [{:keys [data]}]
  (mapv (fn [[k v]] {:name k :items v}) (seq (get-and-reset! data {}))))

