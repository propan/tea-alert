(ns tea-alert.parsers.essenceoftea
  (:require [net.cgrand.enlive-html :as enlive]))

(defn- extract
  [entry selector attr]
  (-> entry
      (enlive/select selector)
      (first)
      (:attrs)
      (attr)))

(defn- extract-item
  [entry]
  {:image (extract entry [:.product-image :img] :src)
   :title (extract entry [:.product-name :a] :title)
   :url   (extract entry [:.product-name :a] :href)})

(defn parse
  [page]
  (->> (enlive/select page [:.product-list-info])
       (map extract-item)))

