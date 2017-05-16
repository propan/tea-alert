(ns tea-alert.storage-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [tea-alert.storage :refer :all]))

(deftest purge-test
  (testing "Remove expired items"
    (let [now (System/currentTimeMillis)]
      (is (= {:a now :c (- now 25)} (#'tea-alert.storage/purge {:a now :b (- now 100) :c (- now 25)} 50 100)))))

  (testing "Remove old items over the size limit"
    (let [now (System/currentTimeMillis)]
      (is (= {:a now :c (- now 1)} (#'tea-alert.storage/purge {:a now :b (- now 2) :c (- now 1)} 50 2))))))

(deftest write-read-test
  (testing "Should return previously written items"
    (let [storage (.start (map->FileStorage {:db-path (str (.getPath (io/resource ".")) "../test-resources/")}))
          _       (write-items storage "test" ["1" "2" "3"])
          _       (write-items storage "test" ["1" "4" "5"])]
      (is (= #{"1" "2" "3" "4" "5"} (read-items storage "test"))))))

