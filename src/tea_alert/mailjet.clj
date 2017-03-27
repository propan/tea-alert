(ns tea-alert.mailjet
  (:require [com.stuartsierra.component :as component]
            [tea-alert.templates.notification :as notification]
            [tea-alert.templates.error-alert :as alert])
  (:import [com.mailjet.client MailjetClient MailjetRequest]
           [com.mailjet.client.resource Contact Email]
           [org.json JSONArray JSONObject]))

(defn- count-items
  [items]
  (->> items
       (map #(count (:items %)))
       (reduce +)))

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

(defrecord MailjetEmailSender [key secret from recipients alert-recipients client]
  component/Lifecycle

  (start [component]
    (println "Initializing Mailjet client")
    (assoc component :client (MailjetClient. key secret)))

  (stop [{:keys [exit-ch] :as component}]
    (println "Destroying Mailjet client")
    (assoc component :client nil)))

(defn create-email-sender
  [key secret from recipients alert-recipients]
  (map->MailjetEmailSender {:key key :secret secret :from from :recipients recipients :alert-recipients alert-recipients}))

(defn send-notification
  [{:keys [client from recipients]} items]
  (let [item-count (count-items items)]
    (doseq [to recipients]
      (println "Sending a notification to:" (:email to) "with" item-count "items")
      (.post client
             (create-request
              {:from    from
               :to      to
               :subject (str "Alert: Discovered " item-count " new items")
               :html    (notification/render to items)})))))

(defn send-error-alert
  [{:keys [client from alert-recipients]} ex]
  (doseq [to alert-recipients]
    (println "Sending an error alert to:" (:email to))
    (.post client
           (create-request
            {:from    from
             :to      to
             :subject "Error: Houston, we've had a problem here"
             :html    (alert/render ex)}))))
