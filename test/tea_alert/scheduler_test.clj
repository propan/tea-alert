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
  (testing "Skips check when it is not needed"
    (with-redefs-fn {#'tea-alert.storage/get-next-check (fn [storage]
                                                          (is (= storage {:storage true}))
                                                          (+ 20000 (System/currentTimeMillis)))
                     #'tea-alert.storage/set-next-check (fn [storage _]
                                                          (is false "Never called"))}
      #(check-for-updates {:storage true} {:sender true})))

  (testing "Send notification when new items are detected"
    (with-redefs-fn {#'tea-alert.scheduler/process-store   (fn [storage {:keys [key name]}]
                                                             (is (= {:storage true} storage))
                                                             (is (contains? #{"bitterleafteas" "chawangshop" "essenceoftea" "yunnansourcing" "white2tea"} key))
                                                             (let [items (if (contains? #{"bitterleafteas" "chawangshop"} key)
                                                                           [key]
                                                                           [])]
                                                               {:name name :items items}))
                     #'tea-alert.mailjet/send-notification (fn [sender items]
                                                             (is (= {:sender true} sender))
                                                             (is (= [{:name "Bitterleaf Teas" :items ["bitterleafteas"]}
                                                                     {:name "Cha Wang Shop" :items ["chawangshop"]}] items)))
                     #'tea-alert.storage/get-next-check    (fn [storage]
                                                             (is (= {:storage true} storage))
                                                             (- (System/currentTimeMillis) 20000))
                     #'tea-alert.storage/set-next-check    (fn [storage next]
                                                             (is (= {:storage true} storage))
                                                             (is (> next (System/currentTimeMillis)))
                                                             (is (< next (+ (System/currentTimeMillis) (* 60000 61)))))}
      #(check-for-updates {:storage true} {:sender true}))))
