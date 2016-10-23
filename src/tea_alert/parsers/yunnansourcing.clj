(ns tea-alert.parsers.yunnansourcing
  (:require [net.cgrand.enlive-html :as enlive]
            [tea-alert.parsers.helpers :refer [extract-with]]))

(defn- extract-item
  [entry]
  {:image (extract-with entry [:.center_block :.product_img_link :img] #(-> % :attrs :src))
   :title (extract-with entry [:.center_block :h3 :a] #(-> % :content first))
   :url   (extract-with entry [:.center_block :h3 :a] #(-> % :attrs :href))
   :price (extract-with entry [:.price] #(-> % :content first))})

(defn parse
  [page]
  (->> (enlive/select page [:.ajax_block_product])
       (map extract-item)))

