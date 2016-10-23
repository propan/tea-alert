(ns tea-alert.parsers.bitterleafteas
  (:require [net.cgrand.enlive-html :as enlive]
            [tea-alert.parsers.helpers :refer [extract-with]]))

(defn- extract-item
  [entry]
  {:image (extract-with entry [:.product-image :img] #(-> % :attrs :src))
   :title (extract-with entry [:h3] #(-> % :content first))
   :url   (extract-with entry [:a.product-loop-title] #(-> % :attrs :href))
   :price (extract-with entry [:.price :.amount] #(enlive/text %))})

(defn parse
  [page]
  (->> (enlive/select page [:.products :.product])
       (map extract-item)))

