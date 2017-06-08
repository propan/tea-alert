(ns tea-alert.storage-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [com.stuartsierra.component :as cm]
            [clj-time.coerce :as c]
            [tea-alert.drivers :as d]
            [tea-alert.storage :refer :all]))

(deftest purge-test
  (testing "Remove expired items"
    (let [now (System/currentTimeMillis)]
      (is (= {:a now :c (- now 25)} (#'tea-alert.storage/purge {:a now :b (- now 100) :c (- now 25)} 50 100)))))

  (testing "Remove old items over the size limit"
    (let [now (System/currentTimeMillis)]
      (is (= {:a now :c (- now 1)} (#'tea-alert.storage/purge {:a now :b (- now 2) :c (- now 1)} 50 2))))))

(deftest index-items-test
  (testing "Returns a sequence of indexed items"
    (with-redefs-fn {#'clj-time.core/now (constantly (c/from-long 893462400000))}
      #(is (= {"test-1" {"123" 893462400000 "456" 893462400000}
               "test-2" {"234" 893462400000 "345" 893462400000}}
              (#'tea-alert.storage/index-items [{:key "test-1" :items ["123" "456"]} {:key "test-2" :items ["234" "345"]}]))))))

(deftest merge-items-test
  (testing "Merges new items with new items dropping expired items"
    (let [now (System/currentTimeMillis)]
      (is (= {"test-1" {"1" now "2" now}
              "test-2" {"2" now "3" now}}
             (#'tea-alert.storage/merge-items {"test-1" {"1" (- now TTL 10) "2" now "3" (- now TTL 10)}
                                               "test-2" {"1" (- now TTL 10) "2" now "3" (- now TTL 10)}}
                                              {"test-1" {"1" now "2" now}
                                               "test-2" {"2" now "3" now}}))))))

(deftest storage-test
  (testing "Stores items in an empty storage"
    (let [s (-> (create-storage nil) (cm/start))]
      (write-items s [{:key "store-1" :items ["1" "2"]} {:key "store-2" :items ["3" "4"]}])
      (is (= #{"1" "2"} (read-items s "store-1")))
      (is (= #{"3" "4"} (read-items s "store-2")))))

  (testing "Stores items in a non-empty storage"
    (let [s (-> (create-storage nil) (cm/start))]
      (doto s
        (write-items [{:key "store-1" :items ["1"]} {:key "store-2" :items ["3"]}])
        (write-items [{:key "store-1" :items ["2"]} {:key "store-2" :items ["4"]}]))
      (is (= #{"1" "2"} (read-items s "store-1")))
      (is (= #{"3" "4"} (read-items s "store-2"))))))

(deftest storage-with-file-driver-test
  (testing "Should return previously written items"
    (let [storage (-> (create-storage (d/create-file-driver (str (.getPath (io/resource ".")) "../test-resources/"))) (cm/start))
          _       (write-items storage [{:key "test" :items ["1" "2" "3"]}])
          _       (write-items storage [{:key "test" :items ["1" "4" "5"]}])
          _       (cm/stop storage)
          storage (-> (create-storage (d/create-file-driver (str (.getPath (io/resource ".")) "../test-resources/"))) (cm/start))]
      (is (= #{"1" "2" "3" "4" "5"} (read-items storage "test"))))))

