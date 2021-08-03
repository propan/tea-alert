(ns tea-alert.parsers.baduchai
  (:require [net.cgrand.enlive-html :as enlive]
            [clojure.string :as s]
            [tea-alert.parsers.helpers :refer [extract-with ifnil make-absolute-url]]))

(defn- extract-item 
  [entry]
  {:image (extract-with entry [:.jupiterx-wc-loop-product-image :img] #(-> % :attrs :src))
   :title (extract-with entry [:.jupiterx-product-container :h2] #(-> % :content first))
   :url   (extract-with entry [:.jupiterx-product-container :a] #(-> % :attrs :href))
   :price (extract-with entry [:.jupiterx-product-container :.amount] #(enlive/text %))})

(defn parse
  [page]
  (->> (enlive/select page [:.product])
       (map extract-item)))

