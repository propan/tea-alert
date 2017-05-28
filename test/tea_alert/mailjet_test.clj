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
  [capture]
  (proxy [MailjetClient] ["" ""]
    (post [request]
      (reset! capture request)
      nil)))

(deftest send-notification-test
  (testing "Sends notification email"
    (let [capture     (atom nil)
          log-capture (atom [])]
      (with-redefs-fn {#'tea-alert.templates.notification/render (fn [to items]
                                                                   (is (= to {:email "bob@te.st"}))
                                                                   (is (= items TEST_STORE_ITEMS))
                                                                   "Rendered email.")

                       #'clojure.core/println                    (fn [& args] (swap! log-capture conj (clojure.string/join " " args)))

                       #'tea-alert.mailjet/create-request        (fn [ctx]
                                                                   (is (= ctx {:from    {:email "alex@te.st"}
                                                                               :to      {:email "bob@te.st"}
                                                                               :subject "Alert: Discovered 3 new items"
                                                                               :html    "Rendered email."}))
                                                                   TEST_MAILJET_REQUEST)}
        #(do
           (send-notification {:client     (mailjet-stub capture)
                               :from       {:email "alex@te.st"}
                               :recipients [{:email "bob@te.st"}]} TEST_STORE_ITEMS)
           (is (= TEST_MAILJET_REQUEST @capture))
           (is (= ["Sending a notification to: bob@te.st with 3 items"] @log-capture)))))))

(deftest send-error-alert-test
  (testing "Sends alert email"
    (let [capture     (atom nil)
          log-capture (atom [])]
      (with-redefs-fn {#'tea-alert.templates.error-alert/render (fn [ex]
                                                                  (is (= ex TEST_EXCEPTION))
                                                                  "Rendered email.")

                       #'clojure.core/println                   (fn [& args] (swap! log-capture conj (clojure.string/join " " args)))

                       #'tea-alert.mailjet/create-request       (fn [ctx]
                                                                  (is (= ctx {:from    {:email "alex@te.st"}
                                                                              :to      {:email "bob@te.st"}
                                                                              :subject "Error: Houston, we've had a problem here"
                                                                              :html    "Rendered email."}))
                                                                  TEST_MAILJET_REQUEST)}
        #(do
           (send-error-alert {:client           (mailjet-stub capture)
                              :from             {:email "alex@te.st"}
                              :alert-recipients [{:email "bob@te.st"}]} TEST_EXCEPTION)
           (is (= TEST_MAILJET_REQUEST @capture))
           (is (= ["Sending an error alert to: bob@te.st"] @log-capture)))))))
