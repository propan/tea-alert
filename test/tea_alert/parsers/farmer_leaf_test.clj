(ns tea-alert.parsers.farmer-leaf-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [net.cgrand.enlive-html :as enlive]
            [tea-alert.parsers.farmer-leaf :refer [parse]]))

(defn absolute-path
  [file]
  (str (.getPath (io/resource ".")) "../test-resources/" file))

(deftest parse-test
  (testing "Correctly parses source page"
    (let [page (-> (absolute-path "farmer-leaf.html") (java.io.File.) (enlive/html-resource) (parse))]
      (is (= [{:image "https://cdn.shopify.com/s/files/1/1318/5761/products/MG_3891_grande.JPG?v=1534420512"
               :title "Spring 2018 Purple Black Tea"
               :url   "https://www.farmer-leaf.com/collections/yunnan-black-tea/products/spring-2018-purple-black-tea"
               :price "-"}
              {:image "https://cdn.shopify.com/s/files/1/1318/5761/products/black_wok_1_grande.jpg?v=1525802334"
               :title "Spring 2018 Jingmai Black Light Roast"
               :url   "https://www.farmer-leaf.com/collections/yunnan-black-tea/products/spring-2018-jingmai-black-light-roast"
               :price "-"}
              {:image "https://cdn.shopify.com/s/files/1/1318/5761/products/black_short_ox_1_grande.jpg?v=1525798906"
               :title "Spring 2018 Jingmai Sun-Dried Black -- Short oxidation"
               :url   "https://www.farmer-leaf.com/collections/yunnan-black-tea/products/spring-2018-jingmai-sun-dried-black-short-oxidation"
               :price "-"}
              {:image "https://cdn.shopify.com/s/files/1/1318/5761/products/black_long_ox_3_grande.jpg?v=1525797932"
               :title "Spring 2018 Sun-Dried Black -- Long Oxidation"
               :url   "https://www.farmer-leaf.com/collections/yunnan-black-tea/products/spring-2018-sun-dried-black-long-oxidation"
               :price "-"}
              {:image "https://cdn.shopify.com/s/files/1/1318/5761/products/jingmai_black_f7ba76b0-4552-44ab-8fd8-0dde49d7b4ca_grande.jpg?v=1478726895"
               :title "Autumn 2017 Dianhong Jingmai Black Tea"
               :url   "https://www.farmer-leaf.com/collections/yunnan-black-tea/products/jingmai-dianhong-black-tea"
               :price "-"}] page)))))
