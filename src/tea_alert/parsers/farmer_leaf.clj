(ns tea-alert.parsers.farmer-leaf
  (:require [net.cgrand.enlive-html :as enlive]
            [clojure.string :as s]
            [tea-alert.parsers.helpers :refer [extract-with ifnil make-absolute-url]]))

(defn- extract-item 
  [entry]
  {:image (extract-with entry [:.product_image :img] #(-> % :attrs :src ifnil (s/replace #"//" "https://")))
   :title (extract-with entry [:.title] #(-> % :content first))
   :url   (extract-with entry [:a] #(-> % :attrs :href ifnil (make-absolute-url "https://www.farmer-leaf.com")))
   :price (extract-with entry [:.money :span] #(-> % :content first ifnil))})

(defn parse
  [page]
  (->> (enlive/select page [:.products :div.thumbnail])
       (map extract-item)))

