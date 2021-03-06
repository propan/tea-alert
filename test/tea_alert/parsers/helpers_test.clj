(ns tea-alert.parsers.helpers-test
  (:require [clojure.test :refer :all]
            [tea-alert.parsers.helpers :refer :all]))

(deftest make-absolute-url-test
  (testing "Makes relative URL absolute"
    (is (= "http://domain.com/home?test=true" (make-absolute-url "/home?test=true" "http://domain.com"))))

  (testing "Leaves absolute URL unmodified"
    (is (= "http://api.domain.com/home?test=true" (make-absolute-url "http://api.domain.com/home?test=true" "http://domain.com")))))

(deftest ifnil-test
  (testing "Returns an empty string if called with nil"
    (is (= "" (ifnil nil))))

  (testing "Returns the original string if called with not nil"
    (is (= "test" (ifnil "test")))))
