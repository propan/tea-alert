(ns tea-alert.buffer-test
  (:require [clojure.test :refer :all]
            [com.stuartsierra.component :as c]
            [tea-alert.buffer :as buf]))

(deftest buffer-test
  (testing "Stores items in an empty buffer"
    (let [b (-> (buf/create-buffer false) (c/start))]
      (buf/put! b [{:name "store-1" :items [1 2]} {:name "store-2" :items [3 4]}])
      (is (= [{:name "store-1" :items [1 2]} {:name "store-2" :items [3 4]}] (buf/peek b)))))

  (testing "Stores items in a non-empty buffer"
    (let [b (-> (buf/create-buffer false) (c/start))]
      (doto b
        (buf/put! [{:name "store-1" :items [1]} {:name "store-2" :items [3]}])
        (buf/put! [{:name "store-1" :items [2]} {:name "store-2" :items [4]}]))
      (is (= [{:name "store-1" :items [1 2]} {:name "store-2" :items [3 4]}] (buf/peek b)))))

  (testing "take! empties a non-empty buffer"
    (let [b (-> (buf/create-buffer false) (c/start))]
      (buf/put! b [{:name "store-1" :items [1 2]} {:name "store-2" :items [3 4]}])
      (is (= [{:name "store-1" :items [1 2]} {:name "store-2" :items [3 4]}] (buf/take! b)))
      (is (= [] (buf/peek b)))))

  (testing "Consecutive take! gets nothing"
    (let [b (-> (buf/create-buffer false) (c/start))]
      (buf/put! b [{:name "store-1" :items [1 2]} {:name "store-2" :items [3 4]}])
      (is (= [{:name "store-1" :items [1 2]} {:name "store-2" :items [3 4]}] (buf/take! b)))
      (is (= [] (buf/take! b))))))

(deftest s3-save-data-test
  (testing "Uploads data to S3"
    (let [capture (atom nil)]
      (with-redefs-fn {#'amazonica.aws.s3/put-object (fn [c & args]
                                                       (is (= {:endpoint "eu-west-1"} c))
                                                       (reset! capture args))}
        #(do
           (#'tea-alert.buffer/s3-save-data {:test true})
           (let [{:keys [bucket-name key input-stream]} @capture]
             (is (= "h-storage" bucket-name))
             (is (= "tea-alert/buffer.edn" key))
             (is (= "{:test true}" (slurp input-stream))))))))

  (testing "Reports errors"
    (let [capture (atom nil)]
      (with-redefs-fn {#'amazonica.aws.s3/put-object (fn [c & args]
                                                       (throw (Exception. "I failed.")))
                       #'clojure.core/println        (fn [& args]
                                                       (reset! capture args))}
        #(do
           (#'tea-alert.buffer/s3-save-data {:test true})
           (is (= ["Failed to upload buffer data to S3: " "I failed."] @capture)))))))

(deftest s3-fetch-data-test
  (testing "Fetches data from S3"
    (let [capture (atom nil)]
      (with-redefs-fn {#'amazonica.aws.s3/get-object (fn [c & args]
                                                       (is (= {:endpoint "eu-west-1"} c))
                                                       (reset! capture args)
                                                       {:input-stream (-> (pr-str {:test true}) (.getBytes "UTF-8") (java.io.ByteArrayInputStream.))})}
        #(do
           (is (= {:test true} (#'tea-alert.buffer/s3-fetch-data)))
           (is (= [:bucket-name "h-storage" :key "tea-alert/buffer.edn"] @capture))))))

  (testing "Reports errors"
    (let [capture (atom nil)]
      (with-redefs-fn {#'amazonica.aws.s3/get-object (fn [c & args]
                                                       (throw (Exception. "I failed.")))
                       #'clojure.core/println        (fn [& args]
                                                       (reset! capture args))}
        #(do
           (is (= {} (#'tea-alert.buffer/s3-fetch-data)))
           (is (= ["Failed to fetch buffer data from S3: " "I failed."] @capture)))))))

