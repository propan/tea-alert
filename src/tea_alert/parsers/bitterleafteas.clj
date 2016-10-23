(ns tea-alert.parsers.bitterleafteas
  (:require [net.cgrand.enlive-html :as enlive]))

(defn- extract-with
  [entry selector efn]
  (-> entry
      (enlive/select selector)
      (first)
      (efn)))

(defn- extract-item
  [entry]
  {:image (extract-with entry [:.product-image :img] #(-> % :attrs :src))
   :title (extract-with entry [:h3] #(-> % :content first))
   :url   (extract-with entry [:a.product-loop-title] #(-> % :attrs :href))})

(defn parse
  [page]
  (->> (enlive/select page [:.products :.product])
       (map extract-item)))

