(ns tea-alert.parsers.badurov
  (:require [net.cgrand.enlive-html :as enlive]
            [clojure.string :as s]
            [tea-alert.parsers.helpers :refer [extract-with ifnil make-absolute-url]]))

(defn- normalize-price
  [p]
  (if-let [[price currency] (-> p (.replace "\u00a0" "_") (s/split #"_"))]
    (str currency price)
    "-")) 

(defn- extract-item 
  [entry]
  {:image (extract-with entry [:.product :.product-loop-thumb :a :img] #(-> % :attrs :src))
   :title (extract-with entry [:.product :.mk-shop-item-detail :h3 :a] #(-> % :content first))
   :url   (extract-with entry [:.product :.mk-shop-item-detail :h3 :a] #(-> % :attrs :href))
   :price (extract-with entry [:.product :.mk-shop-item-detail :.mk-price :.amount] #(-> % :content first normalize-price))})

(defn parse
  [page]
  (->> (enlive/select page [:.products :.product])
       (map extract-item)))
