(ns tea-alert.parsers.white2tea
  (:require [net.cgrand.enlive-html :as enlive]
            [tea-alert.parsers.helpers :refer [extract-with]]))

(defn- extract-item
  [entry]
  {:image (extract-with entry [:.product-image-wrapper :img] #(-> % :attrs :src))
   :title (extract-with entry [:.product-title :a] #(-> % :content first))
   :url   (extract-with entry [:.product-title :a] #(-> % :attrs :href))
   :price (extract-with entry [:.price :.amount] #(enlive/text %))})

(defn parse
  [page]
  (->> (enlive/select page [:div.products-grid :.product])
       (map extract-item)))
