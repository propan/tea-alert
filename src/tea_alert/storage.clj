(ns tea-alert.storage
  (:require [com.stuartsierra.component :as component]
            [clojure.java.io :as io]))

(defn load-data
  []
  (try
    (with-open [r (io/reader "./ta.db")]
      (read (java.io.PushbackReader. r)))
    (catch Exception e
      {})))

(defn save-data
  [data]
  (->> (pr data)
       (with-out-str)
       (spit  "./ta.db"))
  data)

(defrecord FileStorage [data]
  component/Lifecycle

  (start [component]
    (assoc component :data (atom (load-data))))

  (stop [{:keys [data] :as component}]
    (when-not (nil? data)
      (save-data @data))
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
  [{:keys [data]} time]
  (swap! data assoc :next-check time)
  (save-data @data))

(defn read-items
  [{:keys [data]} store]
  (let [key (keyword (str "entries-" store))
        val (key @data)]
    (set val)))

(defn write-items
  [{:keys [data]} store items]
  (let [key (keyword (str "entries-" store))]
    (swap! data assoc key items)
    (save-data @data)))
