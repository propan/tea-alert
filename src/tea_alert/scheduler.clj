(ns tea-alert.scheduler
  (:require [clojure.core.async :as async :refer [chan go-loop <! alt! close!]]
            [clj-time.periodic :as p]
            [clj-time.core :as t]
            [clj-http.client :as client]
            [com.stuartsierra.component :as component]
            [net.cgrand.enlive-html :as enlive]
            [tea-alert.parsers.bitterleafteas :as bitterleafteas]
            [tea-alert.parsers.badurov :as badurov]
            [tea-alert.parsers.chawangshop :as chawangshop]
            [tea-alert.parsers.essenceoftea :as essenceoftea]
            [tea-alert.parsers.farmer-leaf :as farmer-leaf]
            [tea-alert.parsers.moychay :as moychay]
            [tea-alert.parsers.yunnansourcing :as yunnansourcing]
            [tea-alert.parsers.white2tea :as white2tea]
            [tea-alert.storage :as s]
            [tea-alert.buffer :as b]
            [tea-alert.mailjet :as m]))

(def STORES
  [{:name   "Bitterleaf Teas"
    :key    "bitterleafteas"
    :urls   ["http://www.bitterleafteas.com/shop?orderby=date"]
    :parser bitterleafteas/parse}
   
   {:name   "Cha Wang Shop"
    :key    "chawangshop"
    :urls   ["http://www.chawangshop.com/index.php/newest"]
    :parser chawangshop/parse}

   {:name   "The Essence of Tea"
    :key    "essenceoftea"
    :urls   ["https://essenceoftea.com/collections/new-products?view=all"]
    :parser essenceoftea/parse}

   {:name   "Yunnan Sourcing"
    :key    "yunnansourcing"
    :urls   ["https://yunnansourcing.com/collections/new-products?sort_by=created-descending"]
    :parser yunnansourcing/parse}

   {:name   "White2Tea"
    :key    "white2tea"
    :urls   ["http://white2tea.com"]
    :parser white2tea/parse}

   {:name   "MoyChay.RU"
    :key    "moychay"
    :urls   ["https://moychay.com/catalog/new_products"]
    :parser moychay/parse}

   {:name   "БаДу Чай"
    :key    "badurov"
    :urls   ["http://badurov.ru/product-category/tea/krasnyj/?orderby=date"
             "http://badurov.ru/product-category/tea/shen-puer/?orderby=date"
             "http://badurov.ru/product-category/tea/shu-puer/?orderby=date"]
    :parser badurov/parse}

   {:name   "Farmer Leaf"
    :key    "farmerleaf"
    :urls   ["https://www.farmer-leaf.com/collections/yunnan-black-tea"
             "https://www.farmer-leaf.com/collections/ripe-pu-erh-tea"
             "https://www.farmer-leaf.com/collections/tea-cakes"
             "https://www.farmer-leaf.com/collections/loose-leaf-pu-erh"]
    :parser farmer-leaf/parse}])

(def ITEM_VALIDATORS
  {:image #(and (string? %) (clojure.string/starts-with? % "http"))
   :title #(and (string? %) (not (clojure.string/blank? %)))
   :url   #(and (string? %) (clojure.string/starts-with? % "http"))
   :price #(and (string? %) (not (clojure.string/blank? %)))})

(defonce ALERT_THROTTLE (atom {}))

(defn- valid-item?
  [item]
  (reduce (fn [r [k fn]] (if-not (-> item k fn) (reduced false) true)) false ITEM_VALIDATORS))

(defn- update-throttle
  [key now tm]
  (if-let [current (key tm)]
    (assoc tm key {:ts now :throttle (< (t/in-minutes (t/interval (:ts current) now)) 60)})
    (assoc tm key {:ts now :throttle false})))

(defn- purge-throttle
  [threshold tm]
  (reduce (fn [m [k {:keys [ts] :as v}]] (if (t/before? ts threshold) m (assoc m k v))) {} tm))

(defn throttle?
  [store type]
  (let [key       (keyword (str type "-" store))
        now       (t/now)
        threshold (t/plus now (t/minutes -120))]
    (get-in (swap! ALERT_THROTTLE #(->> % (purge-throttle threshold) (update-throttle key now))) [key :throttle])))

(defn fetch-listed-single
  [name key url parser]
  (try
    (-> url (client/get {:insecure?            true
                         :socket-timeout       15000
                         :conn-timeout         15000
                         :conn-request-timeout 15000}) :body enlive/html-snippet parser)
    (catch Exception ex
      (throw (ex-info (str "Failed to fetch listings from '" name "'") {:type  :http
                                                                        :store key
                                                                        :cause ex})))))

(defn fetch-listed
  [{:keys [name key urls parser]}]
  (->> urls
       (map (fn [url] (fetch-listed-single name key url parser)))
       (reduce concat)))

(defn md5-hash
  [str]
  (let [digest (java.security.MessageDigest/getInstance "MD5")
        bytes  (.getBytes str "UTF-8")]
    (format "%032x" (new java.math.BigInteger 1 (.digest digest bytes)) 32)))

;; 

(defn process-store
  [storage {:keys [name key] :as config}]
  (let [pitems (s/read-items storage key)
        citems (->> config fetch-listed (filter valid-item?))
        nitems (filter (complement #(contains? pitems (md5-hash (:url %)))) citems)]
    (when-not (seq citems)
      (throw (ex-info (str "'" name "' parser returned no items") {:type  :parser
                                                                   :store key})))
    {:name name :key key :items nitems}))

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

(defn check-for-updates
  [storage buffer error-fn]
  (println "Crawling web-store pages")
  (let [new-items (->> STORES
                       (map (fn [{:keys [key name] :as store}]
                              (try
                                (process-store storage store)
                                (catch Exception ex
                                  (if-let [data (ex-data ex)]
                                    (error-fn ex)
                                    (error-fn (ex-info (str "Failed to crawl '" name "' store") {:type  :generic
                                                                                                 :store key
                                                                                                 :cause ex})))
                                  {:name name :key key :items []}))))
                       (filter #(seq (:items %))))]
    (if (seq new-items)
      (do
        (println (str "Found " (count new-items) " new items"))
        (s/write-items storage (map (fn [data]
                                      (update data :items #(->> % (map :url) (mapv md5-hash)))) new-items))
        (b/put! buffer new-items))
      (println "No new items are found"))))

(defn send-notifications
  [sender buffer]
  (let [new-items (b/take! buffer)]
    (if (seq new-items)
      (m/send-notification sender new-items)
      (println "No new items are found in the buffer"))))

(defn handle-task-error
  [storage sender ex]
  (let [{:keys [cause type store] :or [type :generic store "unknown"]} (ex-data ex)]
    (if (nil? cause)
      (println "Failed to execute task:" (.getMessage ex))
      (println (str (.getMessage ex) ":") (.getMessage cause)))
    ;; send a notification only if the thottle is open
    (when-not (throttle? store type)
      (m/send-error-alert sender ex))))

(defrecord Scheduler [storage buffer sender exit-chs]
  component/Lifecycle

  (start [component]
    (println "Starting scheduler")
    (let [error-fn #(handle-task-error storage sender %)]
      (assoc component :exit-chs [;; crawl web-stores for new items every 30-60 minutes
                                  (schedule #(check-for-updates storage buffer error-fn)
                                            (p/periodic-seq (t/now) (t/minutes (+ 30 (rand-int 30))))
                                            error-fn)
                                  ;; send out notifications every mornining at 08:00 Moscow time
                                  (schedule #(send-notifications sender buffer)
                                            (->> (p/periodic-seq (.. (t/now)
                                                                     (withZone (org.joda.time.DateTimeZone/forID "Europe/Moscow"))
                                                                     (withTime 8 0 0 0))
                                                                 (-> 1 t/days)))
                                            error-fn)])))

  (stop [{:keys [exit-chs] :as component}]
    (println "Stopping scheduler")
    (doseq [ch exit-chs]
      (close! ch))
    (assoc component :exit-chs nil)))

(defn create-scheduler
  []
  (map->Scheduler {}))

