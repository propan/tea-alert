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
   :recipients
   :alert-recipients])

(defn env-var
  [key]
  (-> key name clojure.string/upper-case (clojure.string/replace #"-" "_")))

(defn read-config
  []
  (reduce #(assoc %1 %2 (-> %2 env-var System/getenv)) {} CONFIGURATION_ITEMS))

(defn- is-valid-address?
  [address]
  (try
    (do
      (.validate address)
      true)
    (catch javax.mail.internet.AddressException ex
      (println "Ignoring '" (.toString address) "' as an invalid email address.")
      false)))

(defn- parse-recipients
  [str]
  (->> str
       (javax.mail.internet.InternetAddress/parse)
       (filter is-valid-address?)
       (map (fn [u] {:name  (or (.getPersonal u) "sweetheart")
                    :email (.getAddress u)}))))

(defn- not-empty-or-nil
  [xs] (when (seq xs) xs))

(defn- process-config
  [{:keys [recipients alert-recipients] :as config}]
  (assoc config
         :recipients       (when recipients
                             (-> recipients parse-recipients not-empty-or-nil))
         :alert-recipients (when alert-recipients
                             (-> alert-recipients parse-recipients not-empty-or-nil))))

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
               (:recipients config)
               (:alert-recipients config))
   :scheduler (component/using
               (create-scheduler :interval 10000)
               [:storage :sender])
   :storage   (create-storage)))

(defonce system-instance nil)

(defn -main
  "The entry-point for 'lein run'"
  [& args]
  (let [config (-> (read-config) process-config)
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
