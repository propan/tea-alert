(ns tea-alert.storage
  (:require [com.stuartsierra.component :as component]
            [clojure.java.io :as io]))

(def TTL (* 60 24 60 60 1000))

(def MAX_STORE_SIZE 200)

(defn- purge
  "Returns a new map with filtered out expired items and limited by the given size."
  [items ttl limit]
  (let [threshold (- (System/currentTimeMillis) ttl)]
    (->> (sort-by val > items)
         (filter #(> (second %) threshold))
         (take limit)
         (into {}))))

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
    (let [db-path (or db-path (System/getenv "TEA_ALERT_DATA") ".")]
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
    (set (keys val))))

(defn write-items
  [{:keys [db-path data]} store items]
  (let [key   (keyword (str "entries-" store))
        now   (System/currentTimeMillis)
        items (reduce (fn [m v] (assoc m v now)) {} items)]
    (swap! data update-in [key] #(purge (merge % items) TTL MAX_STORE_SIZE))
    (save-data db-path @data)))
