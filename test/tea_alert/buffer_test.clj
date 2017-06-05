(ns tea-alert.buffer-test
  (:require [clojure.test :refer :all]
            [com.stuartsierra.component :as c]
            [tea-alert.buffer :as buf]))

(deftest buffer-test
  (testing "Stores items in an empty buffer"
    (let [b (-> (buf/create-buffer nil) (c/start))]
      (buf/put! b [{:name "store-1" :items [1 2]} {:name "store-2" :items [3 4]}])
      (is (= [{:name "store-1" :items [1 2]} {:name "store-2" :items [3 4]}] (buf/peek b)))))

  (testing "Stores items in a non-empty buffer"
    (let [b (-> (buf/create-buffer nil) (c/start))]
      (doto b
        (buf/put! [{:name "store-1" :items [1]} {:name "store-2" :items [3]}])
        (buf/put! [{:name "store-1" :items [2]} {:name "store-2" :items [4]}]))
      (is (= [{:name "store-1" :items [1 2]} {:name "store-2" :items [3 4]}] (buf/peek b)))))

  (testing "take! empties a non-empty buffer"
    (let [b (-> (buf/create-buffer nil) (c/start))]
      (buf/put! b [{:name "store-1" :items [1 2]} {:name "store-2" :items [3 4]}])
      (is (= [{:name "store-1" :items [1 2]} {:name "store-2" :items [3 4]}] (buf/take! b)))
      (is (= [] (buf/peek b)))))

  (testing "Consecutive take! gets nothing"
    (let [b (-> (buf/create-buffer nil) (c/start))]
      (buf/put! b [{:name "store-1" :items [1 2]} {:name "store-2" :items [3 4]}])
      (is (= [{:name "store-1" :items [1 2]} {:name "store-2" :items [3 4]}] (buf/take! b)))
      (is (= [] (buf/take! b))))))

