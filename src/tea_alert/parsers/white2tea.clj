(ns tea-alert.parsers.white2tea
  (:require [net.cgrand.enlive-html :as enlive]
            [tea-alert.parsers.helpers :refer [extract-with ifnil make-absolute-url]]))

(defn- extract-item
  [entry]
  {:image (extract-with entry [:.image-element__wrap :img] #(-> % :attrs :data-src ifnil (make-absolute-url "http://white2tea.com")))
   :title (extract-with entry [:.product-details :.title] #(-> % :content first))
   :url   (extract-with entry [:.product_image :a] #(-> % :attrs :href ifnil (make-absolute-url "http://white2tea.com")))
   :price (extract-with entry [:.product-details :.money] #(enlive/text %))})

(defn parse
  [page]
  (->> (enlive/select page [:div.product-wrap])
       (map extract-item)))
