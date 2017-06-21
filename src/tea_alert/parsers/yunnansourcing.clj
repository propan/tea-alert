(ns tea-alert.parsers.yunnansourcing
  (:require [net.cgrand.enlive-html :as enlive]
            [tea-alert.parsers.helpers :refer [extract-with ifnil make-absolute-url]]))

(defn- extract-item
  [entry]
  {:image (extract-with entry [:.img-link :img] #(->> % :attrs :src (str "http:")))
   :title (extract-with entry [:a.title] #(-> % :content first))
   :url   (extract-with entry [:a.title] #(-> % :attrs :href ifnil (make-absolute-url "https://yunnansourcing.com")))
   :price (extract-with entry [:.price :.money] #(->> % :content first ifnil (clojure.core/re-find #"\$\d+\.\d+")))})

(defn parse
  [page]
  (->> (enlive/select page [:.product])
       (map extract-item)))

