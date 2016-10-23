(ns tea-alert.server
  (:require [clojure.core.async :as async :refer [chan <!! close!]]
            [com.stuartsierra.component :as component]
            [tea-alert.mailjet :refer [create-email-sender]]
            [tea-alert.storage :as s :refer [create-storage]]
            [tea-alert.scheduler :refer [create-scheduler]])
  (:gen-class :main true))

(def CONFIGURATION_ITEMS
  [:mailjet-key
   :mailjet-secret
   :sender-email
   :recipient-name
   :recipient-email])

(defn env-var
  [key]
  (-> key name clojure.string/upper-case (clojure.string/replace #"-" "_")))

(defn read-config
  []
  (reduce #(assoc %1 %2 (-> %2 env-var System/getenv)) {} CONFIGURATION_ITEMS))

(defn validate
  [config]
  (reduce
   #(if-let [val (get config %2)]
      %1
      (conj %1 (str (env-var %2) " is not set")))
   [] CONFIGURATION_ITEMS))

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
   :storage   (create-storage)))

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
      (let [exit-ch (chan)]
        (.addShutdownHook (Runtime/getRuntime) (Thread. #(do
                                                           (component/stop system-instance)
                                                           (close! exit-ch))))
        (alter-var-root #'system-instance
                        (fn [_]
                          (-> (create-system config)
                              (component/start))))
        (<!! exit-ch)))))
