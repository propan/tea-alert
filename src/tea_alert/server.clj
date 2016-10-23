(ns tea-alert.server
  (:require [clojure.core.async :as async :refer [chan go-loop <! alt! close!]]
            [com.stuartsierra.component :as component]
            [tea-alert.mailjet :refer [create-email-sender]]
            [tea-alert.storage :as s :refer [create-storage]]
            [tea-alert.scheduler :refer [create-scheduler]])
  (:gen-class :main true))

(defn read-config
  []
  {:mailjet-key     (System/getenv "MAILJET_KEY")
   :mailjet-secret  (System/getenv "MAILJET_SECRET")
   :sender-email    (System/getenv "SENDER_EMAIL")
   :recipient-name  (or
                     (System/getenv "RECIPIENT_NAME")
                     "Anonymous")
   :recipient-email (System/getenv "RECIPIENT_EMAIL")})


(defn validate
  [config]
  (reduce #(if-let [val (get config %2)]
             %1
             (conj %1 (str (name %2) " is required."))) [] [:mailjet-key
                                                            :mailjet-secret
                                                            :sender-email
                                                            :recipient-name
                                                            :recipient-email]))

(defn create-system
  [config]
  (component/system-map
   :sender    (create-email-sender
               (:mailjet-key config)
               (:mailjet-secret config)
               (:sender-email config)
               {:name  (:recipient-name config)
                :email (:recipient-email config)})
   :scheduler (component/using
               (create-scheduler :interval 10000)
               [:storage :sender])
   :storage (create-storage)))

(defonce system-instance nil)

(defn -main
  "The entry-point for 'lein run'"
  [& args]
  (let [config (read-config)
        errors (validate config)]
    (if (seq errors)
      (do
        (println "Cannot start the system due to:")
        (doseq [error errors]
          (println error)))
      (alter-var-root #'system-instance
                      (fn [_]
                        (-> (create-system config)
                            (component/start)))))))
