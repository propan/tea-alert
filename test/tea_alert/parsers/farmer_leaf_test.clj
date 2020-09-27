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
      (is (= [{:image "https://cdn.shopify.com/s/files/1/1318/5761/products/summer2020pu-erhtea-8_50x.jpg?v=1597582542",
               :title "Spring 2020 Lao Wu Shan",
               :url   "https://www.farmer-leaf.com/collections/yunnan-pu-erh-tea/products/spring-2020-lao-wu-shan",
               :price "$7 USD"}
              {:image "https://cdn.shopify.com/s/files/1/1318/5761/products/laoswrapper-1_50x.jpg?v=1597579257",
               :title "Spring 2020 Huey Wa",
               :url   "https://www.farmer-leaf.com/collections/yunnan-pu-erh-tea/products/spring-2020-huey-wa",
               :price "$9 USD"}
              {:image "https://cdn.shopify.com/s/files/1/1318/5761/products/JingmaiMiyuncake_50x.jpg?v=1591715098",
               :title "Jingmai Miyun",
               :url   "https://www.farmer-leaf.com/collections/yunnan-pu-erh-tea/products/jingmai-miyun-1",
               :price "$6 USD"}
              {:image "https://cdn.shopify.com/s/files/1/1318/5761/products/BulangMengnoycake_50x.jpg?v=1591767409",
               :title "Meng Noy",
               :url   "https://www.farmer-leaf.com/collections/yunnan-pu-erh-tea/products/bulang-meng-noy",
               :price "$6 USD"}
              {:image "https://cdn.shopify.com/s/files/1/1318/5761/products/LafuGushucake_50x.jpg?v=1591771350",
               :title "Lafu",
               :url   "https://www.farmer-leaf.com/collections/yunnan-pu-erh-tea/products/lafu",
               :price ""}
              {:image "https://cdn.shopify.com/s/files/1/1318/5761/products/NannuoGushucake_50x.jpg?v=1591701120",
               :title "Nannuo",
               :url   "https://www.farmer-leaf.com/collections/yunnan-pu-erh-tea/products/nannuo",
               :price "$13 USD"}
              {:image "https://cdn.shopify.com/s/files/1/1318/5761/products/jingmaigulanwrappedcake_50x.jpg?v=1591596640",
               :title "Jingmai Gulan",
               :url   "https://www.farmer-leaf.com/collections/yunnan-pu-erh-tea/products/jingmai-gulan-1",
               :price "$19 USD"}
              {:image "https://cdn.shopify.com/s/files/1/1318/5761/products/MG_3001_50x.jpg?v=1579001070",
               :title "Spring 2018 Ao Ne Me Neglected Gardens",
               :url   "https://www.farmer-leaf.com/collections/yunnan-pu-erh-tea/products/spring-2018-ao-ne-me-neglected-gardens",
               :price "$7 USD"}
              {:image "https://cdn.shopify.com/s/files/1/1318/5761/products/20190730145026_50x.jpg?v=1569283648",
               :title "Spring 2019 Mangjing",
               :url   "https://www.farmer-leaf.com/collections/yunnan-pu-erh-tea/products/spring-2019-mangjing",
               :price "$13 USD"}
              {:image "https://cdn.shopify.com/s/files/1/1318/5761/products/0014_1b9a1adf-143a-4b87-b7c2-5eab0bd443fe_50x.jpg?v=1569278122",
               :title "Spring 2019 Jingmai Miyun",
               :url   "https://www.farmer-leaf.com/collections/yunnan-pu-erh-tea/products/spring-2019-jingmai-miyun",
               :price "$6 USD"}
              {:image "https://cdn.shopify.com/s/files/1/1318/5761/products/miyun_1_50x.jpg?v=1569100771",
               :title "Spring 2018 Jingmai Miyun",
               :url   "https://www.farmer-leaf.com/collections/yunnan-pu-erh-tea/products/spring-2018-jingmai-miyun",
               :price "$7 USD"}] page)))))

