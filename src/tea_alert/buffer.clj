(ns tea-alert.buffer
  (:refer-clojure :exclude [peek])
  (:require [amazonica.aws.s3 :as s3]
            [com.stuartsierra.component :as component]
            [clojure.java.io :as io]))

(defn- s3-save-data
  [data]
  (let [stream (-> (pr-str data) (.getBytes "UTF-8") (java.io.ByteArrayInputStream.))]
    (try
      (s3/put-object {:endpoint "eu-west-1"}
                     :bucket-name "h-storage"
                     :key "tea-alert/buffer.edn"
                     :input-stream stream)
      (catch Exception e
        (println "Failed to upload buffer data to S3: " (.getMessage e))))
    data))

(defn- s3-fetch-data
  []
  (try
    (-> (s3/get-object {:endpoint "eu-west-1"}
                       :bucket-name "h-storage"
                       :key "tea-alert/buffer.edn")
        :input-stream
        (slurp)
        (read-string))
    (catch Exception e
      (println "Failed to fetch buffer data from S3: " (.getMessage e))
      {})))

;; TODO: remove when official implementation is available https://dev.clojure.org/jira/browse/CLJ-1454
(defn- get-and-reset!
  [atm new-value]
  (let [old-value @atm]
    (if (compare-and-set! atm old-value new-value)
      (do
        (.notifyWatches atm old-value new-value)
        old-value)
      (recur atm new-value))))

(defrecord S3Buffer [data remote?]
  component/Lifecycle

  (start [{:keys [remote?] :as component}]
    (let [data (atom (if remote? (s3-fetch-data) {}))]
      (when remote?
        (add-watch data :s3-uploader #(s3-save-data %4)))
      (assoc component :data data)))

  (stop [{:keys [data] :as component}]
    (remove-watch data :s3-uploader)
    (assoc component :data nil)))

(defn create-buffer
  [remote?]
  (map->S3Buffer {:remote? remote?}))

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

