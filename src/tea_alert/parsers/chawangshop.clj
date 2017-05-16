(ns tea-alert.parsers.chawangshop
  (:require [net.cgrand.enlive-html :as enlive]
            [tea-alert.parsers.helpers :refer [extract-with]]))

(defn- extract-item
  [entry]
  {:image (extract-with entry [:img.product-image-photo] #(-> % :attrs :src))
   :title (extract-with entry [:.product-item-name :a] #(-> % :content first (clojure.string/replace "\n" "") (clojure.string/trim)))
   :url   (extract-with entry [:.product-item-name :a] #(-> % :attrs :href))
   :price (extract-with entry [:.price] #(-> % :content first))})

(defn parse
  [page]
  (->> (enlive/select page [:.products-grid :ol.product-items :li.product-item])
       (map extract-item)))

