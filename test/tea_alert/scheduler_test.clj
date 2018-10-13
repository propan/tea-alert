(ns tea-alert.scheduler-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as t]
            [tea-alert.scheduler :refer :all]))

(def TEST_STORE_ITEMS
  [{:image "http://store.com/1.jpg" :title "Tea #1" :url "http://store.com/1" :price "$20"}
   {:image "http://store.com/2.jpg" :title "Tea #2" :url "http://store.com/2" :price "$10"}
   {:image "store.com/2.jpg"        :title "Broken" :url "store.com/2"        :price ""}])

(def TEST_STORES
  [{:name   "Test Store"
    :key    "test-store"
    :urls   ["http://www.store.com/shop/1" "http://www.store.com/shop/2"]
    :parser (fn [page] TEST_STORE_ITEMS)}])

(deftest update-throttle-test
  (testing "Sets throttle to false when there is no known state"
    (let [now (t/now)]
      (is (= {:http-error {:ts now :throttle false}} (#'tea-alert.scheduler/update-throttle :http-error now {})))))

  (testing "Sets throttle to true when there is an event within last hour"
    (let [now    (t/now)
          before (t/plus now (t/minutes -15))]
      (is (= {:http-error {:ts now :throttle true}} (#'tea-alert.scheduler/update-throttle :http-error now {:http-error {:ts before :throttle false}})))))

  (testing "Sets throttle to false when there is no event within last hour"
    (let [now    (t/now)
          before (t/plus now (t/minutes -61))]
      (is (= {:http-error {:ts now :throttle false}} (#'tea-alert.scheduler/update-throttle :http-error now {:http-error {:ts before :throttle false}}))))))

(deftest purge-throttle-test
  (testing "Removes stale throttle values"
    (let [now       (t/now)
          threshold (t/plus now (t/minutes -60))]
      (is (= {:ok-1 {:ts now}
              :ok-2 {:ts now}}
             (#'tea-alert.scheduler/purge-throttle threshold
                                                   {:ok-1     {:ts now}
                                                    :ok-2     {:ts now}
                                                    :not-ok-1 {:ts (t/plus now (t/minutes -75))}
                                                    :not-ok-2 {:ts (t/plus now (t/minutes -80))}}))))))

(deftest throttle-test
  (testing "Initial request is not throttled"
    (with-redefs-fn {#'tea-alert.scheduler/ALERT_THROTTLE (atom {})}
      #(is (= false (throttle? "store" :parser)))))

  (testing "Consequent requests are throttled"
    (with-redefs-fn {#'tea-alert.scheduler/ALERT_THROTTLE (atom {})}
      #(are [x y] (= x y)
         false (throttle? "store" :parser)
         true  (throttle? "store" :parser)
         false (throttle? "store" :http)
         true  (throttle? "store" :parser)
         true  (throttle? "store" :http))))

  (testing "Requests are not throttled after threshold"
    (with-redefs-fn {#'tea-alert.scheduler/ALERT_THROTTLE (atom {(keyword (str :parser "-" "store")) {:ts (t/plus (t/now) (t/minutes -65))}
                                                                 (keyword (str :http "-" "store"))   {:ts (t/plus (t/now) (t/minutes -75))}})}
      #(are [x y] (= x y)
         false (throttle? "store" :parser)
         false (throttle? "store" :http)
         true  (throttle? "store" :parser)
         true  (throttle? "store" :http)))))

(deftest valid-item-test
  (let [valid-item {:image "http://store.com/item.jpg" :title "Item" :url "https://store.com/item" :price "$20"}]
    (testing "Returns true for valid item"
      (is (= true (#'tea-alert.scheduler/valid-item? valid-item))))

    (testing "Returns false if :image is missing or invalid"
      (is (= false (#'tea-alert.scheduler/valid-item? (dissoc valid-item :image))))
      (is (= false (#'tea-alert.scheduler/valid-item? (assoc valid-item :image "store.com/item.jpg")))))

    (testing "Returns false if :title is missing or blank"
      (is (= false (#'tea-alert.scheduler/valid-item? (dissoc valid-item :title))))
      (is (= false (#'tea-alert.scheduler/valid-item? (assoc valid-item :title "")))))

    (testing "Returns false if :url is missing or blank"
      (is (= false (#'tea-alert.scheduler/valid-item? (dissoc valid-item :url))))
      (is (= false (#'tea-alert.scheduler/valid-item? (assoc valid-item :url "store.com/item")))))

    (testing "Returns false if :price is missing or blank"
      (is (= false (#'tea-alert.scheduler/valid-item? (dissoc valid-item :price))))
      (is (= false (#'tea-alert.scheduler/valid-item? (assoc valid-item :price "")))))))

(deftest fetch-listed-single-test
  (testing "Returns an empty list when fetching fails"
    (with-redefs-fn {#'clj-http.client/get (fn [url options]
                                             (is (= {:insecure? true :socket-timeout 15000 :conn-timeout 15000 :conn-request-timeout 15000} options))
                                             (throw (Exception. "Troubles!")))}
      #(is (thrown-with-msg? Exception #"Failed to fetch listings from 'Test Store'" (fetch-listed (first TEST_STORES))))))

  (testing "Returns result of the parser"
    (with-redefs-fn {#'clj-http.client/get (fn [url options]
                                             (is (= {:insecure? true :socket-timeout 15000 :conn-timeout 15000 :conn-request-timeout 15000} options))
                                             {:body "something"})}
      (let [{:keys [name key urls parser]} (first TEST_STORES)]
        #(is (= TEST_STORE_ITEMS (fetch-listed-single name key (first urls) parser)))))))

(deftest fetch-listed-test
  (testing "Returns results fetched from multiple URLs"
    (with-redefs-fn {#'tea-alert.scheduler/fetch-listed-single (fn [name key url parser]
                                                                 (case url
                                                                   "http://www.store.com/shop/1" (take 2 TEST_STORE_ITEMS)
                                                                   "http://www.store.com/shop/2" (drop 2 TEST_STORE_ITEMS)))}
      #(is (= TEST_STORE_ITEMS (fetch-listed (first TEST_STORES)))))))

(deftest process-store-test
  (testing "Throws an exception when parser returns no items"
    (with-redefs-fn {#'tea-alert.scheduler/fetch-listed (constantly nil)
                     #'tea-alert.storage/read-items     (fn [storage key] #{"3e0564120f97ff822d1b1b4a0cccb8d5"})}
      #(is (thrown-with-msg? Exception #"'Test Store' parser returned no items" (process-store {:storage true} (first TEST_STORES))))))

  (testing "Returns new items when they are detected"
    (with-redefs-fn {#'tea-alert.scheduler/fetch-listed (fn [conf]
                                                          (is (= conf (first TEST_STORES)))
                                                          TEST_STORE_ITEMS)
                     #'tea-alert.storage/read-items     (fn [storage key]
                                                          (is (= storage {:storage true}))
                                                          (is (= key "test-store"))
                                                          #{"3e0564120f97ff822d1b1b4a0cccb8d5"})}
      #(is (= {:name "Test Store" :key "test-store" :items [(first TEST_STORE_ITEMS)]} (process-store {:storage true} (first TEST_STORES))))))

  (testing "Returns no items when no changes detected"
    (with-redefs-fn {#'tea-alert.scheduler/fetch-listed (fn [conf]
                                                          (is (= conf (first TEST_STORES)))
                                                          TEST_STORE_ITEMS)
                     #'tea-alert.storage/read-items     (fn [storage key]
                                                          (is (= storage {:storage true}))
                                                          (is (= key "test-store"))
                                                          #{"eeaa43c224b477e3fc644118231c8ebf" "3e0564120f97ff822d1b1b4a0cccb8d5"})}
      #(is (= {:name "Test Store" :key "test-store" :items []} (process-store {:storage true} (first TEST_STORES)))))))

(deftest check-for-updates-test
  (testing "Stores new items to the buffer and storage when new items are detected"
    (let [buf-capture     (atom nil)
          storage-capture (atom nil)
          log-capture     (atom [])]
      (with-redefs-fn {#'tea-alert.scheduler/process-store (fn [storage {:keys [key name]}]
                                                             (is (= {:storage true} storage))
                                                             (is (contains? #{"bitterleafteas" "chawangshop" "essenceoftea" "yunnansourcing" "white2tea" "moychay"} key))
                                                             (let [items (if (contains? #{"bitterleafteas" "chawangshop"} key) [{:url key}] [])]
                                                               {:name name :key key :items items}))

                       #'clojure.core/println              (fn [& args] (swap! log-capture conj (clojure.string/join " " args)))

                       #'tea-alert.buffer/put!             (fn [buffer items]
                                                             (is (= {:buffer true} buffer))
                                                             (reset! buf-capture items))

                       #'tea-alert.storage/write-items     (fn [storage items]
                                                             (is (= {:storage true} storage))
                                                             (reset! storage-capture items))}
        #(do
           (check-for-updates {:storage true} {:buffer true} (fn [ex] (is false "Error function should not be called")))
           (is (= [{:name "Bitterleaf Teas" :key "bitterleafteas" :items [{:url "bitterleafteas"}]}
                   {:name "Cha Wang Shop" :key "chawangshop" :items [{:url "chawangshop"}]}] @buf-capture))
           (is (= [{:name "Bitterleaf Teas" :key "bitterleafteas" :items ["79416e5e9b32667cb5e176d7be99e1d3"]}
                   {:name "Cha Wang Shop" :key "chawangshop" :items ["2ed3975efbdcb9ac977021c209d131e7"]}] @storage-capture))
           (is (= ["Crawling web-store pages" "Found 2 new items"] @log-capture))))))

  (testing "Notifies about errors if fetching fails"
    (let [buf-capture     (atom nil)
          storage-capture (atom nil)
          error-capture   (atom nil)
          log-capture     (atom [])]
      (with-redefs-fn {#'tea-alert.scheduler/process-store (fn [storage {:keys [key name]}]
                                                             (is (= {:storage true} storage))
                                                             (is (contains? #{"bitterleafteas" "chawangshop" "essenceoftea" "yunnansourcing" "white2tea" "moychay"} key))
                                                             (if (= "white2tea" key)
                                                               (throw (Exception. "Errors are unavoidable."))
                                                               (let [items (if (contains? #{"bitterleafteas" "chawangshop"} key) [{:url  key}] [])]
                                                                 {:name name :key key :items items})))

                       #'clojure.core/println              (fn [& args] (swap! log-capture conj (clojure.string/join " " args)))

                       #'tea-alert.buffer/put!             (fn [buffer items]
                                                             (is (= {:buffer true} buffer))
                                                             (reset! buf-capture items))

                       #'tea-alert.storage/write-items     (fn [storage items]
                                                             (is (= {:storage true} storage))
                                                             (reset! storage-capture items))}
        #(do
           (check-for-updates {:storage true} {:buffer true} (fn [ex] (reset! error-capture ex)))
           (is (= [{:name "Bitterleaf Teas" :key "bitterleafteas" :items [{:url "bitterleafteas"}]}
                   {:name "Cha Wang Shop" :key "chawangshop" :items [{:url "chawangshop"}]}] @buf-capture))
           (is (= [{:name "Bitterleaf Teas" :key "bitterleafteas" :items ["79416e5e9b32667cb5e176d7be99e1d3"]}
                   {:name "Cha Wang Shop" :key "chawangshop" :items ["2ed3975efbdcb9ac977021c209d131e7"]}] @storage-capture))
           (is (= "Failed to crawl 'White2Tea' store" (when-let [ex @error-capture] (.getMessage ex))))
           (is (= ["Crawling web-store pages" "Found 2 new items"] @log-capture)))))))

(deftest send-notifications-test
  (testing "Sends notification if new items are detected"
    (let [capture (atom nil)]
      (with-redefs-fn {#'tea-alert.buffer/take!              (fn [buffer]
                                                               (is (= {:buffer true} buffer))
                                                               [{:name "store" :items [1 2 3]}])

                       #'tea-alert.mailjet/send-notification (fn [sender items]
                                                               (is (= {:sender true} sender))
                                                               (reset! capture items))}
        #(do
           (send-notifications {:sender true} {:buffer true})
           (is (= [{:name "store" :items [1 2 3]}] @capture))))))

  (testing "Does nothing if no new items are detected"
    (let [log-capture (atom [])]
      (with-redefs-fn {#'tea-alert.buffer/take!              (fn [buffer]
                                                               (is (= {:buffer true} buffer))
                                                               [])

                       #'clojure.core/println              (fn [& args] (swap! log-capture conj (clojure.string/join " " args)))

                       #'tea-alert.mailjet/send-notification (fn [sender items]
                                                               (is false "Error function should not be called"))}
        #(do
           (send-notifications {:sender true} {:buffer true})
           (is (= ["No new items are found in the buffer"] @log-capture)))))))

(deftest handle-task-error-test
  (testing "Produces correct logs when called with a generic exception"
    (let [capture     (atom nil)
          log-capture (atom [])]
      (with-redefs-fn {#'clojure.core/println               (fn [& args] (swap! log-capture conj (clojure.string/join " " args)))

                       #'tea-alert.scheduler/throttle?      (constantly false)

                       #'tea-alert.mailjet/send-error-alert (fn [sender ex]
                                                              (is (= {:sender true} sender))
                                                              (reset! capture ex))}
        #(do
           (handle-task-error {:storage true} {:sender true} (Exception. "Bad error!"))
           (is (= "Bad error!" (.getMessage @capture)))
           (is (= ["Failed to execute task: Bad error!"] @log-capture))))))

  (testing "Produces correct logs when called with an ext exception"
    (let [capture     (atom nil)
          log-capture (atom [])]
      (with-redefs-fn {#'clojure.core/println               (fn [& args] (swap! log-capture conj (clojure.string/join " " args)))

                       #'tea-alert.scheduler/throttle?      (constantly false)

                       #'tea-alert.mailjet/send-error-alert (fn [sender ex]
                                                              (is (= {:sender true} sender))
                                                              (reset! capture ex))}
        #(do
           (handle-task-error {:storage true} {:sender true} (ex-info "Very bad error" {:cause (Exception. "Bad error!")}))
           (is (= "Very bad error" (.getMessage @capture)))
           (is (= ["Very bad error: Bad error!"] @log-capture))))))

  (testing "Does not send a notification when throttle is closed"
    (let [log-capture (atom [])]
      (with-redefs-fn {#'clojure.core/println               (fn [& args] (swap! log-capture conj (clojure.string/join " " args)))

                       #'tea-alert.scheduler/throttle?      (constantly true)

                       #'tea-alert.mailjet/send-error-alert (fn [sender ex]
                                                              (is false "This function should not be called"))}
        #(do
           (handle-task-error {:storage true} {:sender true} (ex-info "Very bad error" {:cause (Exception. "Bad error!")}))
           (is (= ["Very bad error: Bad error!"] @log-capture)))))))
