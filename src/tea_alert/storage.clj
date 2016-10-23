(ns tea-alert.storage
  (:require [com.stuartsierra.component :as component]
            [taoensso.carmine :as car :refer (wcar)]))

(defn- long-or-zero
  [str]
  (try
    (Long/parseLong str)
    (catch Exception e
      0)))

(defrecord RedisStorage [host port db connection]
  component/Lifecycle

  (start [component]
    (println (format "Connecting to Redis %s:%s DB:%s" host port db))
    (let [connection {:pool {} :spec {:host host :port port :db db}}]
      (assoc component :connection connection)))

  (stop [{:keys [connection] :as component}]
    (when-not (nil? connection)
      (println "Disconnecting from Redis"))
    (assoc component :connection nil)))

(defn create-storage
  [& {:keys [host port db] :or {host "127.0.0.1" port 6379 db 0}}]
  (map->RedisStorage {:host host :port port :db db}))

(defn get-next-check
  [{:keys [connection]}]
  (if-let [val (wcar connection (car/get "ta:next-check"))]
    (long-or-zero val)
    (System/currentTimeMillis)))

(defn set-next-check
  [{:keys [connection]} time]
  (wcar connection (car/set "ta:next-check" time)))

(defn read-items
  [{:keys [connection]} store]
  (let [key (str "ta:entries:" store)
        val (wcar connection (car/smembers key))]
    (set val)))

(defn write-items
  [{:keys [connection]} store items]
  (let [key (str "ta:entries:" store)]
    (wcar connection
          (car/del key)
          (apply car/sadd key items))))
