(ns tea-alert.parsers.helpers
  (:require [net.cgrand.enlive-html :as enlive]))

(defn extract-with
  [entry selector efn]
  (-> entry
      (enlive/select selector)
      (first)
      (efn)))
