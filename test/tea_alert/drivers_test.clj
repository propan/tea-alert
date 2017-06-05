(ns tea-alert.drivers-test
  (:require [clojure.test :refer :all]
            [tea-alert.drivers :as d]))

(deftest s3-driver-save-test
  (testing "Uploads data to S3"
    (let [driver  (d/create-s3-driver "eu-west-1" "h-storage" "tea-alert/buffer.edn")
          capture (atom nil)]
      (with-redefs-fn {#'amazonica.aws.s3/put-object (fn [c & args]
                                                       (is (= {:endpoint "eu-west-1"} c))
                                                       (reset! capture args))}
        #(do
           (d/save driver {:test true})
           (let [{:keys [bucket-name key input-stream metadata]} @capture]
             (is (= "h-storage" bucket-name))
             (is (= "tea-alert/buffer.edn" key))
             (is (= {:content-length 12} metadata))
             (is (= "{:test true}" (slurp input-stream))))))))

  (testing "Reports errors"
    (let [driver      (d/create-s3-driver "eu-west-1" "h-storage" "tea-alert/buffer.edn")
          log-capture (atom [])]
      (with-redefs-fn {#'amazonica.aws.s3/put-object (fn [c & args]
                                                       (throw (Exception. "I failed.")))
                       
                       #'clojure.core/println        (fn [& args] (swap! log-capture conj (clojure.string/join " " args)))}
        #(do
           (d/save driver {:test true})
           (is (= ["Failed to upload data to 'tea-alert/buffer.edn' file on S3: I failed."] @log-capture)))))))

(deftest s3-driver-load-test
  (testing "Fetches data from S3"
    (let [driver  (d/create-s3-driver "eu-west-1" "h-storage" "tea-alert/buffer.edn")
          capture (atom nil)]
      (with-redefs-fn {#'amazonica.aws.s3/get-object (fn [c & args]
                                                       (is (= {:endpoint "eu-west-1"} c))
                                                       (reset! capture args)
                                                       {:input-stream (-> (pr-str {:test true}) (.getBytes "UTF-8") (java.io.ByteArrayInputStream.))})}
        #(do
           (is (= {:test true} (d/load driver)))
           (is (= [:bucket-name "h-storage" :key "tea-alert/buffer.edn"] @capture))))))

  (testing "Reports errors"
    (let [driver      (d/create-s3-driver "eu-west-1" "h-storage" "tea-alert/buffer.edn")
          log-capture (atom [])]
      (with-redefs-fn {#'amazonica.aws.s3/get-object (fn [c & args]
                                                       (throw (Exception. "I failed.")))

                       #'clojure.core/println        (fn [& args] (swap! log-capture conj (clojure.string/join " " args)))}
        #(do
           (is (= {} (d/load driver)))
           (is (= ["Failed to fetch data from 'tea-alert/buffer.edn' file on S3: I failed."] @log-capture)))))))

(deftest file-driver-save-test
  (testing "Saves data to a file"
    (let [driver  (d/create-file-driver "/data")
          capture (atom nil)]
      (with-redefs-fn {#'clojure.core/spit (fn [file data]
                                             (is (= "/data/ta.db" file))
                                             (reset! capture data))}
        #(do
           (d/save driver {:test true})
           (is (= "{:test true}" @capture))))))

  (testing "Reports errors"
    (let [driver      (d/create-file-driver "/data")
          log-capture (atom [])]
      (with-redefs-fn {#'clojure.core/spit    (fn [& args]
                                                (throw (Exception. "I failed.")))
                       
                       #'clojure.core/println (fn [& args] (swap! log-capture conj (clojure.string/join " " args)))}
        #(do
           (d/save driver {:test true})
           (is (= ["Failed to save data to '/data/ta.db' file: I failed."] @log-capture)))))))

(deftest file-driver-load-test
  (testing "Loads data from a file"
    (let [driver  (d/create-file-driver "/data")]
      (with-redefs-fn {#'clojure.java.io/reader (fn [file]
                                                  (is (= "/data/ta.db" file))
                                                  (-> (pr-str {:test true}) (java.io.StringReader.)))}
        #(is (= {:test true} (d/load driver))))))

  (testing "Reports errors"
    (let [driver      (d/create-file-driver "/data")
          log-capture (atom [])]
      (with-redefs-fn {#'clojure.java.io/reader (fn [file]
                                                  (throw (Exception. "I failed.")))

                       #'clojure.core/println   (fn [& args] (swap! log-capture conj (clojure.string/join " " args)))}
        #(do
           (is (= {} (d/load driver)))
           (is (= ["Failed to load data from '/data/ta.db' file: I failed."] @log-capture)))))))
