(ns tea-alert.parsers.white2tea-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [net.cgrand.enlive-html :as enlive]
            [tea-alert.parsers.white2tea :refer [parse]]))


(defn absolute-path
  [file]
  (str (.getPath (io/resource ".")) "../test-resources/" file))

(deftest parse-test
  (testing "Correctly parses source page"
    (let [page (-> (absolute-path "white2tea.html") (java.io.File.) (enlive/html-resource) (parse))]
      (is (= [{:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/2020-Loyal-Soldier-Puerh-Tea_1600x.jpg?v=1599826148",
               :title "2020 Loyal Soldier",
               :url   "http://white2tea.com/collections/latest-additions/products/2020-loyal-soldier",
               :price "$8.75"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/2020-Brown-Sugar-Small-Batch-Shou_1600x.jpg?v=1599826145",
               :title "2020 Brown Sugar",
               :url   "http://white2tea.com/collections/latest-additions/products/2020-brown-sugar",
               :price "$4.00"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/2020-Zero-Sum-White-Tea_1600x.jpg?v=1599826143",
               :title "2020 Zero Sum",
               :url   "http://white2tea.com/collections/latest-additions/products/2020-zero-sum",
               :price "$16.50"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/2020-Camphornought-Shu_1600x.jpg?v=1599826140",
               :title "2020 Camphornought",
               :url   "http://white2tea.com/collections/latest-additions/products/2020-camphornought",
               :price "$4.00"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/2020-Camphornought-Puer-Mini_1600x.jpg?v=1599826138",
               :title "2020 Camphornought Mini",
               :url   "http://white2tea.com/collections/latest-additions/products/2020-camphornought-mini",
               :price "$1.10"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/2020-Prosbloom-Tea_1600x.jpg?v=1599826135",
               :title "2020 Prosbloom",
               :url   "http://white2tea.com/collections/latest-additions/products/2020-prosbloom",
               :price "$2.65"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/2020-Prosbloom-Puer-Mini-2_1600x.jpg?v=1599826133",
               :title "2020 Prosbloom Mini",
               :url   "http://white2tea.com/collections/latest-additions/products/2020-prosbloom-mini",
               :price "$0.65"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/2020-Planetary-Shark-Feed-shu_1600x.jpg?v=1599826131",
               :title "2020 Planetary Shark Feed",
               :url   "http://white2tea.com/collections/latest-additions/products/2020-planetary-shark-feed",
               :price "$3.75"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/2020-Planetary-Shark-Feed-Mini_1600x.jpg?v=1599826129",
               :title "2020 Planetary Shark Feed Mini",
               :url   "http://white2tea.com/collections/latest-additions/products/2020-planetary-shark-feed-mini",
               :price "$1.00"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/2020-Waffles-Puerh_1600x.jpg?v=1599826126",
               :title "2020 Waffles",
               :url   "http://white2tea.com/collections/latest-additions/products/2020-waffles",
               :price "$2.20"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/Daily-Jin-Jun-Mei_1600x.jpg?v=1597924293",
               :title "Daily Jinjunmei",
               :url   "http://white2tea.com/collections/latest-additions/products/daily-jinjunmei",
               :price "$6.50"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/2020-Astro-Red-Kittens-Black-Tea_1600x.jpg?v=1597924291",
               :title "2020 Astro Red",
               :url   "http://white2tea.com/collections/latest-additions/products/2020-astro-red",
               :price "$24.60"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/Spiced-Lapsang_1600x.jpg?v=1597924289",
               :title "Spiced Lapsang",
               :url   "http://white2tea.com/collections/latest-additions/products/spiced-lapsang",
               :price "$7.50"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/Florist-Lapsang-Souchong_05bbe8ed-6bca-4381-816a-3aece8e4c5e4_1600x.jpg?v=1597924287",
               :title "Florist Lapsang",
               :url   "http://white2tea.com/collections/latest-additions/products/florist-lapsang-1",
               :price "$8.50"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/Strawberry-Lapsang_1600x.jpg?v=1597924286",
               :title "Strawberry Lapsang",
               :url   "http://white2tea.com/collections/latest-additions/products/florist-lapsang",
               :price "$9.50"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/Fruitbomb-Lapsang_5f5044cc-f5ae-4664-a181-64728f8b507d_1600x.jpg?v=1597749170",
               :title "Fruit Bomb Lapsang",
               :url   "http://white2tea.com/collections/latest-additions/products/fruit-bomb-lapsang",
               :price "$9.50"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/Traditional-Lapsang_c319a64b-6f16-4433-bc07-758c2fc86cd0_1600x.jpg?v=1597748962",
               :title "Traditional Lapsang",
               :url   "http://white2tea.com/collections/latest-additions/products/traditional-lapsang",
               :price "$6.50"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/Pinesap-Lapsang_f89f6202-1fdc-4208-b292-1a5dbd2ffe0b_1600x.jpg?v=1597748715",
               :title "Pine Sap Lapsang",
               :url   "http://white2tea.com/collections/latest-additions/products/pine-sap-lapsang",
               :price "$7.50"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/Smoked-Lapsang-1_0c5e3eeb-894b-4011-8079-923c540a279c_1600x.jpg?v=1597748448",
               :title "Smoked Lapsang",
               :url   "http://white2tea.com/collections/latest-additions/products/smoked-lapsang",
               :price "$9.50"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/2020-Demon-Slayer-Minis-Huangpian-tea_1600x.jpg?v=1595070643",
               :title "2020 Demon Slayer Minis",
               :url   "http://white2tea.com/collections/latest-additions/products/2020-demon-slayer-minis",
               :price "$1.35"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/2020-Unicorn_30fc073f-5a83-46cb-b440-a8e72365f221_1600x.jpg?v=1595070640",
               :title "2020 Unicorn",
               :url   "http://white2tea.com/collections/latest-additions/products/2020-unicorn",
               :price "$49.00"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/Pomelo-Dancong_1600x.jpg?v=1595070639",
               :title "Pomelo Dancong",
               :url   "http://white2tea.com/collections/latest-additions/products/pomelo-dancong",
               :price "$13.50"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/Parasol-dancong-niang-zai-san_1600x.jpg?v=1595070637",
               :title "Parasol Dancong",
               :url   "http://white2tea.com/collections/latest-additions/products/parasol-dancong",
               :price "$14.00"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/Full-House-Dancong_1600x.jpg?v=1595070636",
               :title "Full House Dancong",
               :url   "http://white2tea.com/collections/latest-additions/products/full-house-dancong",
               :price "$15.50"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/Moon-Bloom-Dancong_1600x.jpg?v=1595070634",
               :title "Moon Bloom Dancong",
               :url   "http://white2tea.com/collections/latest-additions/products/moon-bloom-dancong",
               :price "$9.50"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/Song-Zhong-Dancong_1600x.jpg?v=1595070632",
               :title "Song Zhong Dancong",
               :url   "http://white2tea.com/collections/latest-additions/products/song-zhong-dancong",
               :price "$12.50"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/Daily-Milan-Dancong-Oolong-Tea_1600x.jpg?v=1595070631",
               :title "Daily Milan Dancong",
               :url   "http://white2tea.com/collections/latest-additions/products/daily-milan-dancong",
               :price "$6.50"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/No2-Milan-Dancong-Oolong_1600x.jpg?v=1595070629",
               :title "No.2 Milan Dancong",
               :url   "http://white2tea.com/collections/latest-additions/products/no-2-milan-dancong-oolong",
               :price "$15.50"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/Duck-Shit-Dancong_1600x.jpg?v=1595070628",
               :title "Duck Shit Dancong",
               :url   "http://white2tea.com/collections/latest-additions/products/duck-shhh-oolong-ya-shi-xiang-dancong",
               :price "$11.00"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/2020-Moon-Waffles-8_1600x.jpg?v=1591621875",
               :title "2020 Moon Waffles",
               :url   "http://white2tea.com/collections/latest-additions/products/2020-moon-waffles",
               :price "$5.00"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/2020-Lucky-Puppy_1600x.jpg?v=1591621873",
               :title "2020 Lucky Puppy",
               :url   "http://white2tea.com/collections/latest-additions/products/2020-lucky-puppy",
               :price "$33.00"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/2020-Inb4_1600x.jpg?v=1591621870",
               :title "2020 inb4",
               :url   "http://white2tea.com/collections/latest-additions/products/2020-inb4",
               :price "$26.00"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/2020-Demon-Slayer_1600x.jpg?v=1591621868",
               :title "2020 Demon Slayer",
               :url   "http://white2tea.com/collections/latest-additions/products/2020-demon-slayer",
               :price "$5.00"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/2020-Astro-Kittens_1600x.jpg?v=1591621865",
               :title "2020 Astro Kittens",
               :url   "http://white2tea.com/collections/latest-additions/products/2020-astro-kittens",
               :price "$21.50"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/2020-Mall-Ninja-Dojo_1600x.jpg?v=1591621863",
               :title "2020 Mall Ninja Dojo",
               :url   "http://white2tea.com/collections/latest-additions/products/2020-mall-ninja-dojo",
               :price "$7.00"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/2020-Horse-Girl-Clique_1600x.jpg?v=1591621860",
               :title "2020 Horse Girl Clique",
               :url   "http://white2tea.com/collections/latest-additions/products/2020-horse-girl-clique",
               :price "$7.00"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/SnowFlake-Dancong-Oolong-tea_1600x.jpg?v=1582591314",
               :title "Snowflake Dancong",
               :url   "http://white2tea.com/products/snowflake-dancong-oolong",
               :price "$5.50"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/2018-Waffles_1600x.jpg?v=1580926743",
               :title "2018 Waffles",
               :url   "http://white2tea.com/products/2018-waffles",
               :price "$3.00"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/2017-Big-Green-Hype-Tea_791ceb5e-7310-438d-86b9-e3a3010c5aa1_1600x.jpg?v=1581314760",
               :title "2017 Big Green Hype",
               :url   "http://white2tea.com/products/2017-big-green-hype",
               :price "$6.00"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/2018-TurtleDove-Yunnan-white-tea-1_1600x.jpg?v=1596018332",
               :title "2018 Turtle Dove",
               :url   "http://white2tea.com/products/2018-turtle-dove",
               :price "$4.50"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/2020-La-Sombra-Raw_1600x.jpg?v=1589623364",
               :title "2020 La Sombra",
               :url   "http://white2tea.com/products/2020-la-sombra",
               :price "$7.50"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/Daily-Jin-Jun-Mei_1600x.jpg?v=1597924293",
               :title "Daily Jinjunmei",
               :url   "http://white2tea.com/products/daily-jinjunmei",
               :price "$6.50"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/2019-Natural-Redhead-Tea_1600x.jpg?v=1596018343",
               :title "2019 Natural Redhead",
               :url   "http://white2tea.com/products/2019-natural-redhead",
               :price "$4.70"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/2020-Horse-Girl-Clique_1600x.jpg?v=1591621860",
               :title "2020 Horse Girl Clique",
               :url   "http://white2tea.com/products/2020-horse-girl-clique",
               :price "$7.00"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/2020-Ecce-Puer_1600x.jpg?v=1589623352",
               :title "2020 Ecce Puer",
               :url   "http://white2tea.com/products/2020-ecce-puer",
               :price "$10.00"}
              {:image "http://cdn.shopify.com/s/files/1/0313/6064/7305/products/2020-Hotline-Space-Coyote-Sheng-Puer-Tea_1600x.jpg?v=1589623359",
               :title "2020 Hotline Space Coyote",
               :url   "http://white2tea.com/products/2020-hotline-space-coyote",
               :price "$5.35"}] page)))))

