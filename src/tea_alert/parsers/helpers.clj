(ns tea-alert.parsers.helpers
  (:require [net.cgrand.enlive-html :as enlive]))

(defn extract-with
  [entry selector efn]
  (-> entry
      (enlive/select selector)
      (first)
      (efn)))

(defn make-absolute-url
  [url base]
  (.toString  (.resolve (java.net.URI/create base) (java.net.URI/create url))))

(defn ifnil
  [s]
  (or s ""))
