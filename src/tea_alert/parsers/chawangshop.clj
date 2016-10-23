(ns tea-alert.parsers.chawangshop
  (:require [net.cgrand.enlive-html :as enlive]
            [tea-alert.parsers.helpers :refer [extract-with]]))

(defn- extract-item
  [entry]
  {:image (extract-with entry [:.product-image :img] #(-> % :attrs :src))
   :title (extract-with entry [:.product-name :a] #(-> % :content first))
   :url   (extract-with entry [:.product-name :a] #(-> % :attrs :href))
   :price (extract-with entry [:.price] #(-> % :content first))})

(defn parse
  [page]
  (->> (enlive/select page [:.std :.item])
       (map extract-item)))

