(ns tea-alert.scheduler-test
  (:require [clojure.test :refer :all]
            [tea-alert.scheduler :refer :all]))

(def TEST_STORE_ITEMS
  [{:image "http://store.com/1.jpg" :title "Tea #1" :url "http://store.com/1" :price "$20"}
   {:image "http://store.com/2.jpg" :title "Tea #2" :url "http://store.com/2" :price "$10"}])

(def TEST_STORES
  [{:name   "Test Store"
    :key    "test-store"
    :url    "http://www.store.com/shop"
    :parser (fn [page] TEST_STORE_ITEMS)}])

(deftest fetch-listed-test
  (testing "Returns an empty list when fetching fails"
    (with-redefs-fn {#'clj-http.client/get (fn [url] (throw (Exception. "Troubles!")))}
      #(is (= [] (fetch-listed (first TEST_STORES))))))

  (testing "Returns result of the parser"
    (with-redefs-fn {#'clj-http.client/get (fn [url] {:body "something"})}
      #(is (= TEST_STORE_ITEMS (fetch-listed (first TEST_STORES)))))))

(deftest process-store-test
  (testing "Returns new items when they are detected"
    (with-redefs-fn {#'tea-alert.scheduler/fetch-listed (fn [conf]
                                                          (is (= conf (first TEST_STORES)))
                                                          TEST_STORE_ITEMS)
                     #'tea-alert.storage/read-items     (fn [storage key]
                                                          (is (= storage {:storage true}))
                                                          (is (= key "test-store"))
                                                          #{"3e0564120f97ff822d1b1b4a0cccb8d5"})
                     #'tea-alert.storage/write-items    (fn [storage key items]
                                                          (is (= storage {:storage true}))
                                                          (is (= key "test-store"))
                                                          (is (= items ["eeaa43c224b477e3fc644118231c8ebf" "3e0564120f97ff822d1b1b4a0cccb8d5"])))}
      #(is (= {:name "Test Store" :items [(first TEST_STORE_ITEMS)]} (process-store {:storage true} (first TEST_STORES))))))

  (testing "Returns no items when no changes detected"
    (with-redefs-fn {#'tea-alert.scheduler/fetch-listed (fn [conf]
                                                          (is (= conf (first TEST_STORES)))
                                                          TEST_STORE_ITEMS)
                     #'tea-alert.storage/read-items     (fn [storage key]
                                                          (is (= storage {:storage true}))
                                                          (is (= key "test-store"))
                                                          #{"eeaa43c224b477e3fc644118231c8ebf" "3e0564120f97ff822d1b1b4a0cccb8d5"})
                     #'tea-alert.storage/write-items    (fn [storage key items]
                                                          (is (= storage {:storage true}))
                                                          (is (= key "test-store"))
                                                          (is (= items [])))}
      #(is (= {:name "Test Store" :items []} (process-store {:storage true} (first TEST_STORES)))))))

(deftest check-for-updates-test
  (testing "Stores new items to the buffer when new items are detected"
    (let [capture (atom nil)]
      (with-redefs-fn {#'tea-alert.scheduler/process-store (fn [storage {:keys [key name]}]
                                                             (is (= {:storage true} storage))
                                                             (is (contains? #{"bitterleafteas" "chawangshop" "essenceoftea" "yunnansourcing" "white2tea" "moychay"} key))
                                                             (let [items (if (contains? #{"bitterleafteas" "chawangshop"} key) [key] [])]
                                                               {:name name :items items}))

                       #'tea-alert.buffer/put!             (fn [buffer items]
                                                             (is (= {:buffer true} buffer))
                                                             (reset! capture items))}
        #(do
           (check-for-updates {:storage true} {:buffer true} (fn [ex] (is false "Error function should not be called")))
           (is (= [{:name "Bitterleaf Teas" :items ["bitterleafteas"]}
                   {:name "Cha Wang Shop" :items ["chawangshop"]}] @capture))))))

  (testing "Notifies about errors if fetching fails"
    (let [buffer-capture (atom nil)
          error-capture  (atom nil)]
      (with-redefs-fn {#'tea-alert.scheduler/process-store (fn [storage {:keys [key name]}]
                                                             (is (= {:storage true} storage))
                                                             (is (contains? #{"bitterleafteas" "chawangshop" "essenceoftea" "yunnansourcing" "white2tea" "moychay"} key))
                                                             (if (= "white2tea" key)
                                                               (throw (Exception. "Errors are unavoidable."))
                                                               (let [items (if (contains? #{"bitterleafteas" "chawangshop"} key) [key] [])]
                                                                 {:name name :items items})))

                       #'tea-alert.buffer/put!             (fn [buffer items]
                                                             (is (= {:buffer true} buffer))
                                                             (reset! buffer-capture items))}
        #(do
           (check-for-updates {:storage true} {:buffer true} (fn [ex] (reset! error-capture ex)))
           (is (= [{:name "Bitterleaf Teas" :items ["bitterleafteas"]}
                   {:name "Cha Wang Shop" :items ["chawangshop"]}] @buffer-capture))
           (is (= "Failed to crawl White2Tea store" (when-let [ex @error-capture] (.getMessage ex)))))))))

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
    (let [capture (atom nil)]
      (with-redefs-fn {#'tea-alert.buffer/take!              (fn [buffer]
                                                               (is (= {:buffer true} buffer))
                                                               [])

                       #'clojure.core/println                (fn [& args]
                                                               (reset! capture args))

                       #'tea-alert.mailjet/send-notification (fn [sender items]
                                                               (is false "Error function should not be called"))}
        #(do
           (send-notifications {:sender true} {:buffer true})
           (is (= ["No new items are found in the buffer"] @capture)))))))
