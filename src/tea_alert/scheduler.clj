(ns tea-alert.scheduler
  (:require [clojure.core.async :as async :refer [chan go-loop <! alt! close!]]
            [clj-http.client :as client]
            [com.stuartsierra.component :as component]
            [net.cgrand.enlive-html :as enlive]
            [tea-alert.parsers.bitterleafteas :as bitterleafteas]
            [tea-alert.parsers.chawangshop :as chawangshop]
            [tea-alert.parsers.essenceoftea :as essenceoftea]
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
    :url    "http://yunnansourcing.com/en/new-products"
    :parser yunnansourcing/parse}

   {:name   "White2Tea"
    :key    "white2tea"
    :url    "http://white2tea.com"
    :parser white2tea/parse}])

(defn fetch-listed
  [{:keys [name url parser]}]
  (try
    (-> url client/get :body enlive/html-snippet parser)
    (catch Exception e
      (println "Failed to fetch listings from" name ":" e)
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
    {:store name :items nitems}))

(defn- schedule
  [task interval]
  (let [exit-ch (chan)]
    (go-loop [timeout-ch (async/timeout interval)]
      (alt!
        exit-ch    (println "Task cancelled.")
        timeout-ch (do
                     (try
                       (task)
                       (catch Exception e
                         (println "Failed to execute task:" e)))
                     (recur (async/timeout interval)))))
    exit-ch))

(defn- within-an-hour
  []
  (let [now (System/currentTimeMillis)]
    (+ now (* 60000 (+ 30 (rand-int 30))))))

(defn check-for-updates
  [storage sender]
  (when (<= (s/get-next-check storage) (System/currentTimeMillis))
    (println "Crawling web-store pages")
    (let [new-items (map #(process-store storage %) STORES)]
      (if (seq new-items)
        (m/send-notification sender new-items)
        (println "No new items are found")))
    (s/set-next-check storage (within-an-hour))))

(defrecord Scheduler [interval storage sender exit-ch]
  component/Lifecycle

  (start [component]
    (println (format "Starting scheduler with interval: %dms" interval))
    (assoc component :exit-ch (schedule #(check-for-updates storage sender) interval)))

  (stop [{:keys [exit-ch] :as component}]
    (println "Stopping scheduler")
    (close! exit-ch)
    (assoc component :exit-ch nil)))

(defn create-scheduler
  [& {:keys [interval] :or {interval 60000}}]
  (map->Scheduler {:interval interval}))
