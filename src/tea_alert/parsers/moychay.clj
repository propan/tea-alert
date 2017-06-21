(ns tea-alert.parsers.moychay
  (:require [net.cgrand.enlive-html :as enlive]
            [tea-alert.parsers.helpers :refer [extract-with ifnil make-absolute-url]]))

(defn- extract-item
  [entry]
  {:image (extract-with entry [:.img :img] #(-> % :attrs :src ifnil (make-absolute-url "https://moychay.com")))
   :title (extract-with entry [:.description] #(-> % :content first))
   :url   (extract-with entry [:.group-description :a] #(-> % :attrs :href ifnil (make-absolute-url "https://moychay.com")))
   :price (extract-with entry [:.price :span] (fn [el] (str "$" (-> el :content first ifnil (clojure.string/replace "\n" "")))))})

(defn parse
  [page]
  (->> (enlive/select page [:.list-products :.item])
       (map extract-item)))

