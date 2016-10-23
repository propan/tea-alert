(ns tea-alert.mailjet
  (:require [com.stuartsierra.component :as component]
            [tea-alert.templates.notification :refer [render]])
  (:import [com.mailjet.client MailjetClient MailjetRequest]
           [com.mailjet.client.resource Contact Email]
           [org.json JSONArray JSONObject]))

(defn- create-recipient
  [{:keys [email name]}]
  (doto (JSONObject.)
    (.put Contact/EMAIL email)
    (.put Contact/NAME name)))

(defn- create-recipients
  [recipients]
  (reduce #(.put %1 (create-recipient %2)) (JSONArray.) recipients))

(defn- create-request
  [{:keys [from to subject text html]}]
  (doto (MailjetRequest. Email/resource)
    (.property Email/FROMEMAIL from)
    (.property Email/FROMNAME "Tea Alert")
    (.property Email/SUBJECT subject)
    (.property Email/TEXTPART text)
    (.property Email/HTMLPART html)
    (.property Email/RECIPIENTS (create-recipients [to]))))

(defrecord MailjetEmailSender [key secret from to client]
  component/Lifecycle

  (start [component]
    (println "Initializing Mailjet client")
    (assoc component :client (MailjetClient. key secret)))

  (stop [{:keys [exit-ch] :as component}]
    (println "Destroying Mailjet client")
    (assoc component :client nil)))

(defn create-email-sender
  [key secret from to]
  (map->MailjetEmailSender {:key key :secret secret :from from :to to}))

(defn send-notification
  [{:keys [client from to]} items]
  (println "Sending a notification to: " (:email to))
  (.post client
         (create-request
          {:from    from
           :to      to
           :subject (str "Alert: Discovered " (count items) " new items")
           :html    (render to items)})))
