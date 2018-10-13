(ns tea-alert.parsers.essenceoftea
  (:require [net.cgrand.enlive-html :as enlive]
            [clojure.string :as s]
            [tea-alert.parsers.helpers :refer [extract-with ifnil make-absolute-url]]))

(defn- pick
  [srcset]
  (-> srcset (s/split #" \d+w(, )?") (first) (ifnil) (s/replace #"//" "https://")))

(defn- extract-item
  [entry]
  {:image (extract-with entry [:.product :.image :img] #(-> % :attrs :srcset pick))
   :title (extract-with entry [:.product :.name :a] #(-> % :content first))
   :url   (extract-with entry [:.product :.name :a] #(-> % :attrs :href ifnil (make-absolute-url "https://essenceoftea.com")))
   :price (extract-with entry [:.product :.price :.price-new] #(-> % :content first))})

(defn parse
  [page]
  (->> (enlive/select page [:.product-grid :.product])
       (map extract-item)))

