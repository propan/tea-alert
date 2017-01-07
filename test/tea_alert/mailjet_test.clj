(ns tea-alert.mailjet-test
  (:require [clojure.test :refer :all]
            [tea-alert.mailjet :refer :all])
  (:import [com.mailjet.client MailjetClient MailjetRequest]
           [com.mailjet.client.resource Email]))

(def TEST_MAILJET_REQUEST
  (MailjetRequest. Email/resource))

(def TEST_STORE_ITEMS
  [{:name "Store 1" :items [1 2]}
   {:name "Store 2" :items [3]}])

(def TEST_EXCEPTION
  (Exception.))

(defn- mailjet-stub
  []
  (proxy [MailjetClient] ["" ""]
    (post [request]
      (is (= request TEST_MAILJET_REQUEST))
      nil)))

(deftest send-notification-test
  (testing "Sends notification email"
    (with-redefs-fn {#'tea-alert.templates.notification/render (fn [to items]
                                                                 (is (= to {:email "bob@te.st"}))
                                                                 (is (= items TEST_STORE_ITEMS))
                                                                 "Rendered email.")
                     #'tea-alert.mailjet/create-request        (fn [ctx]
                                                                 (is (= ctx {:from    {:email "alex@te.st"}
                                                                             :to      {:email "bob@te.st"}
                                                                             :subject "Alert: Discovered 3 new items"
                                                                             :html    "Rendered email."}))
                                                                 TEST_MAILJET_REQUEST)}
      #(send-notification {:client (mailjet-stub)
                           :from   {:email "alex@te.st"}
                           :to     {:email "bob@te.st"}} TEST_STORE_ITEMS))))

(deftest send-error-alert-test
  (testing "Sends alert email"
    (with-redefs-fn {#'tea-alert.templates.error-alert/render (fn [ex]
                                                                (is (= ex TEST_EXCEPTION))
                                                                "Rendered email.")
                     #'tea-alert.mailjet/create-request       (fn [ctx]
                                                                (is (= ctx {:from    {:email "alex@te.st"}
                                                                            :to      {:email "bob@te.st"}
                                                                            :subject "Error: Houston, we've had a problem here"
                                                                            :html    "Rendered email."}))
                                                                TEST_MAILJET_REQUEST)}
      #(send-error-alert {:client (mailjet-stub)
                          :from   {:email "alex@te.st"}
                          :to     {:email "bob@te.st"}} TEST_EXCEPTION))))
