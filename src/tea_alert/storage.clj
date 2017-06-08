(ns tea-alert.storage
  (:require [com.stuartsierra.component :as component]
            [clj-time.coerce :as c]
            [clj-time.core :as t]
            [tea-alert.drivers :as d]))

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

(defn- index-items
  [items]
  (let [now (c/to-long (t/now))]
    (reduce (fn [m {:keys [key items]}]
              (assoc m key (reduce (fn [m v] (assoc m v now)) {} items))) {} items)))

(defn- merge-items
  [m data]
  (reduce (fn [a [key items]]
            (update a key #(-> % (merge items) (purge TTL MAX_STORE_SIZE)))) m data))

(defrecord Storage [data driver]
  component/Lifecycle

  (start [component]
    (let [data (atom (if (nil? driver) {} (d/load driver)))]
      (when-not (nil? driver)
        (add-watch data :storage-sync #(d/save driver %4)))
      (assoc component :data data)))

  (stop [{:keys [data] :as component}]
    (remove-watch data :storage-sync)
    (assoc component :data nil)))

(defn create-storage
  [driver]
  (map->Storage {:driver driver}))

(defn read-items
  [{:keys [data]} store]
  (-> @data (get store) keys set))

(defn write-items
  [{:keys [data]} items]
  (let [indexed-items (index-items items)]
    (swap! data merge-items indexed-items)))

