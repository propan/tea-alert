(ns tea-alert.parsers.yunnansourcing
  (:require [net.cgrand.enlive-html :as enlive]))

(defn- extract-with
  [entry selector efn]
  (-> entry
      (enlive/select selector)
      (first)
      (efn)))

(defn- extract-item
  [entry]
  {:image (extract-with entry [:.center_block :.product_img_link :img] #(-> % :attrs :src))
   :title (extract-with entry [:.center_block :h3 :a] #(-> % :content first))
   :url   (extract-with entry [:.center_block :h3 :a] #(-> % :attrs :href))})

(defn parse
  [page]
  (->> (enlive/select page [:.ajax_block_product])
       (map extract-item)))

