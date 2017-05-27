(ns tea-alert.scheduler
  (:require [clojure.core.async :as async :refer [chan go-loop <! alt! close!]]
            [clj-time.periodic :as p]
            [clj-time.core :as t]
            [clj-http.client :as client]
            [com.stuartsierra.component :as component]
            [net.cgrand.enlive-html :as enlive]
            [tea-alert.parsers.bitterleafteas :as bitterleafteas]
            [tea-alert.parsers.chawangshop :as chawangshop]
            [tea-alert.parsers.essenceoftea :as essenceoftea]
            [tea-alert.parsers.moychay :as moychay]
            [tea-alert.parsers.yunnansourcing :as yunnansourcing]
            [tea-alert.parsers.white2tea :as white2tea]
            [tea-alert.storage :as s]
            [tea-alert.mailjet :as m]))

(def STORES
  [{:name   "Bitterleaf Teas"
    :key    "bitterleafteas"
    :url    "http://www.bitterleafteas.com/shop?orderby=date"
    :parser bitterleafteas/parse}
   
   {:name   "Cha Wang Shop"
    :key    "chawangshop"
    :url    "http://www.chawangshop.com/index.php/newest"
    :parser chawangshop/parse}

   {:name   "The Essence of Tea"
    :key    "essenceoftea"
    :url    "https://www.essenceoftea.com/new/"
    :parser essenceoftea/parse}

   {:name   "Yunnan Sourcing"
    :key    "yunnansourcing"
    :url    "https://yunnansourcing.com/collections/new-products?sort_by=created-descending"
    :parser yunnansourcing/parse}

   {:name   "White2Tea"
    :key    "white2tea"
    :url    "http://white2tea.com"
    :parser white2tea/parse}

   {:name   "MoyChay.RU"
    :key    "moychay"
    :url    "https://moychay.com/catalog/new_products"
    :parser moychay/parse}])

(defn fetch-listed
  [{:keys [name url parser]}]
  (try
    (-> url client/get :body enlive/html-snippet parser)
    (catch Exception e
      ;; TODO: throw the exception to trigger an alert
      (println "Failed to fetch listings from" name ":" (.getMessage e))
      [])))

(defn md5-hash
  [str]
  (let [digest (java.security.MessageDigest/getInstance "MD5")
        bytes  (.getBytes str "UTF-8")]
    (format "%032x" (new java.math.BigInteger 1 (.digest digest bytes)) 32)))

;; 

(defn process-store
  [storage {:keys [name key] :as config}]
  (let [pitems (s/read-items storage key)
        citems (fetch-listed config)
        nitems (filter (complement #(contains? pitems (md5-hash (:url %)))) citems)]
    (when (seq nitems)
      (s/write-items storage key (->> citems (map :url) (map md5-hash))))
    {:name name :items nitems}))

(defn interval-ms
  [start end]
  (if (t/before? end start)
    0
    (t/in-millis (t/interval start end))))

(defn- schedule
  [task time-seq error-fn]
  (let [exit-ch (chan)]
    (go-loop [now      (t/now)
              time-seq (drop-while #(t/before? % now) time-seq)]
      (when-let [[next & rest] (seq time-seq)]
        (alt!
          exit-ch                                (println "Task cancelled.")
          (async/timeout (interval-ms now next)) (do
                                                   (try
                                                     (task)
                                                     (catch Exception e
                                                       (error-fn e)))
                                                   (recur (t/now) rest)))))
    exit-ch))

(defn- within-an-hour
  []
  (let [now (System/currentTimeMillis)]
    (+ now (* 60000 (+ 30 (rand-int 30))))))

(defn check-for-updates
  [storage notification-fn error-fn]
  (when (<= (s/get-next-check storage) (System/currentTimeMillis))
    (println "Crawling web-store pages")
    (let [new-items (->> STORES
                         (map (fn [store]
                                (try
                                  (process-store storage store)
                                  (catch Exception e
                                    (error-fn (ex-info (str "Failed to crawl " (:name store) " store") {:cause e}))
                                    {:name (:name store) :items []}))))
                         (filter #(seq (:items %))))]
      (if (seq new-items)
        (notification-fn new-items)
        (println "No new items are found")))
    (s/set-next-check storage (within-an-hour))))

(defn handle-task-error
  [storage sender ex]
  (println "Failed to execute task:" ex)
  ;; TODO: throttle emails with errors per shop+error type
  (m/send-error-alert sender ex))

(defrecord Scheduler [interval storage sender exit-chs]
  component/Lifecycle

  (start [component]
    (println (format "Starting scheduler with interval: %dms" interval))
    (let [error-fn        #(handle-task-error storage sender %)
          notification-fn #(m/send-notification sender %)]
      (assoc component :exit-chs [(schedule #(check-for-updates storage notification-fn error-fn)
                                            (p/periodic-seq (t/now) (t/millis interval))
                                            error-fn)])))

  (stop [{:keys [exit-chs] :as component}]
    (println "Stopping scheduler")
    (doseq [ch exit-chs]
      (close! ch))
    (assoc component :exit-chs nil)))

(defn create-scheduler
  [& {:keys [interval] :or {interval 60000}}]
  (map->Scheduler {:interval interval}))

