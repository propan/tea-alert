(ns tea-alert.parsers.farmer-leaf
  (:require [net.cgrand.enlive-html :as enlive]
            [clojure.string :as s]
            [tea-alert.parsers.helpers :refer [extract-with ifnil make-absolute-url]]))
(defn- extract-item 
  [entry]
  {:image (extract-with entry [:.product_image :img] #(-> % :attrs :src ifnil (s/replace #"//" "https://")))
   :title (extract-with entry [(enlive/attr= :itemprop "name")] #(-> % :content first))
   :url   (extract-with entry [(enlive/attr= :itemprop "url")] #(-> % :attrs :href ifnil (make-absolute-url "https://www.farmer-leaf.com")))
   :price "-"})

(defn parse
  [page]
  (->> (enlive/select page [(enlive/attr= :itemprop "itemListElement")])
       (map extract-item)))
