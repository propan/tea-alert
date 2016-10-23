(ns tea-alert.parsers.chawangshop
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
   :title (extract-with entry [:.product-name :a] #(-> % :content first))
   :url   (extract-with entry [:.product-name :a] #(-> % :attrs :href))})

(defn parse
  [page]
  (->> (enlive/select page [:.std :.item])
       (map extract-item)))

