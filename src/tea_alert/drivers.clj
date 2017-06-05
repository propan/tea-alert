(ns tea-alert.drivers
  (:refer-clojure :exclude [load])
  (:require [amazonica.aws.s3 :as s3]
            [com.stuartsierra.component :as component]
            [clojure.java.io :as io]))

(defprotocol StorageDriver
  (save [_ data])
  (load [_]))

(deftype S3Driver [endpoint bucket-name key]
  StorageDriver

  (save [_ data]
    (let [data (-> (pr-str data) (.getBytes "UTF-8"))]
      (try
        (s3/put-object {:endpoint endpoint}
                       :bucket-name bucket-name
                       :key key
                       :input-stream (java.io.ByteArrayInputStream. data)
                       :metadata {:content-length (alength data)})
        (catch Exception ex
          (println (str "Failed to upload data to '" key "' file on S3:") (.getMessage ex))))
      data))

  (load [_]
    (try
      (-> (s3/get-object {:endpoint endpoint}
                         :bucket-name bucket-name
                         :key key)
          :input-stream
          (slurp)
          (read-string))
      (catch Exception ex
        (println (str "Failed to fetch data from '" key "' file on S3:") (.getMessage ex))
        {}))))

(defn create-s3-driver
  [endpoint bucket-name key]
  (S3Driver. endpoint bucket-name key))

(deftype FileDriver [path]
  StorageDriver

  (save [_ data]
    (try
      (->> (pr data)
           (with-out-str)
           (spit (str path "/ta.db")))
      (catch Exception ex
        (println (str "Failed to save data to '" path "/ta.db' file:") (.getMessage ex))))
    data)

  (load [_]
    (try
      (with-open [r (io/reader (str path "/ta.db"))]
        (read (java.io.PushbackReader. r)))
      (catch Exception ex
        (println (str "Failed to load data from '" path "/ta.db' file:") (.getMessage ex))
        {}))))

(defn create-file-driver
  [db-path]
  (let [db-path (or db-path (System/getenv "TEA_ALERT_DATA") ".")]
    (FileDriver. db-path)))
