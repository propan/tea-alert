(defproject tea-alert "0.1.0-SNAPSHOT"
  :description "a notification system that informs you when new stuff is up for sale in your favourite tea store"
  :url "https://github.com/propan/tea-alert"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-http "3.3.0"]
                 [enlive "1.1.6"]
                 [org.clojure/core.async "0.2.395"]
                 [com.stuartsierra/component "0.3.1"]

                 [hiccup "1.0.5"]
                 [javax.mail/mail "1.4.7"]
                 [com.mailjet/mailjet-client "4.0.5"]]

  :main tea-alert.server
  :aot [tea-alert.server])
