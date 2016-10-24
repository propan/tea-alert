(ns tea-alert.storage
  (:require [com.stuartsierra.component :as component]
            [clojure.java.io :as io]))

(defn- load-data
  [path]
  (try
    (with-open [r (io/reader (str path "/ta.db"))]
      (read (java.io.PushbackReader. r)))
    (catch Exception e
      {})))

(defn- save-data
  [path data]
  (->> (pr data)
       (with-out-str)
       (spit  (str path "/ta.db")))
  data)

(defrecord FileStorage [db-path data]
  component/Lifecycle

  (start [component]
    (let [db-path (or (System/getenv "TEA_ALERT_DATA") ".")]
      (assoc component
             :data (atom (load-data db-path))
             :db-path db-path)))

  (stop [{:keys [db-path data] :as component}]
    (when-not (nil? data)
      (save-data db-path @data))
    (assoc component :data nil)))

(defn create-storage
  []
  (map->FileStorage {}))

(defn get-next-check
  [{:keys [data]}]
  (if-let [val (:next-check @data)]
    val
    (System/currentTimeMillis)))

(defn set-next-check
  [{:keys [db-path data]} time]
  (swap! data assoc :next-check time)
  (save-data db-path @data))

(defn read-items
  [{:keys [data]} store]
  (let [key (keyword (str "entries-" store))
        val (key @data)]
    (set val)))

(defn write-items
  [{:keys [db-path data]} store items]
  (let [key (keyword (str "entries-" store))]
    (swap! data assoc key items)
    (save-data db-path @data)))
