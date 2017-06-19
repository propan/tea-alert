(ns tea-alert.templates.error-alert-test
  (:require [clojure.test :refer :all]
            [tea-alert.templates.error-alert :refer :all]))

(deftest render-test
  (testing "Renders correctly a regular exception"
    (let [message (render (Exception. "Things are not always great"))]
      (is (clojure.string/includes? message "Things are not always great"))
      (is (clojure.string/includes? message "tea_alert.templates.error_alert_test"))
      (is (clojure.string/includes? message "tea_alert.templates.error_alert_test"))))

  (testing "Renders correctly an ex-info exception"
    (let [message (render (ex-info "Other things might be great" {:cause (Exception. "Things are not always great")}))]
      (is (clojure.string/includes? message "Other things might be great"))
      (is (clojure.string/includes? message "Things are not always great"))
      (is (not (clojure.string/includes? message "tea_alert.templates.error_alert_test")))))

  (testing "Renders correctly an exception without a stack trace"
    (let [message (render (doto (NullPointerException.) (.setStackTrace (into-array java.lang.StackTraceElement []))))]
      (is (clojure.string/includes? message "java.lang.NullPointerException"))
      (is (not (clojure.string/includes? message "tea_alert.templates.error_alert_test"))))))
