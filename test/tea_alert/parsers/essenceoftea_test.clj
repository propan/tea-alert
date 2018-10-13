(ns tea-alert.parsers.essenceoftea-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [net.cgrand.enlive-html :as enlive]
            [tea-alert.parsers.essenceoftea :refer [parse]]))

(defn absolute-path
  [file]
  (str (.getPath (io/resource ".")) "../test-resources/" file))

(deftest parse-test
  (testing "Correctly parses source page"
    (let [page (-> (absolute-path "essenceoftea.html") (java.io.File.) (enlive/html-resource) (parse))]
      (is (= [{:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/3-169_medium@2x.jpg?v=1538199054"
               :title "2003 Farmer Style Liu Bao"
               :url   "https://essenceoftea.com/collections/new-products/products/2003-farmer-style-liu-bao"
               :price "$9.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-317_medium@2x.jpg?v=1537940390"
               :title "2010 Da Xue Shan Wild Puerh 1kg Brick"
               :url   "https://essenceoftea.com/collections/new-products/products/2010-da-xue-shan-wild-puerh-brick"
               :price "$8.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/yixing_teapot_hongni_factory_11-2_medium@2x.jpg?v=1536131261"
               :title "200ml Hongni Yixing Teapot Factory 1"
               :url   "https://essenceoftea.com/collections/new-products/products/200ml-hongni-yixing-teapot-factory-1"
               :price "$112.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/yixing_teapot_hongni_factory_14_medium@2x.jpg?v=1536129480"
               :title "170ml Hongni Yixing Teapot Factory 1"
               :url   "https://essenceoftea.com/collections/new-products/products/170ml-hongni-yixing-teapot-factory-1"
               :price nil}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-316_medium@2x.jpg?v=1533270615"
               :title "2014 Da Meng Long Gushu"
               :url   "https://essenceoftea.com/collections/new-products/products/2014-da-meng-long-gushu"
               :price "$9.50"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/Eraser_medium@2x.jpg?v=1533023323"
               :title "2005 Jingmai Gushu"
               :url   "https://essenceoftea.com/collections/new-products/products/2005-jingmai-gushu"
               :price "$16.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-312_058a63fb-bba2-4500-bdcf-dd55f18336e2_medium@2x.jpg?v=1532412728"
               :title "40ml Vintage Octagonal Cups - Goldfish"
               :url   "https://essenceoftea.com/collections/new-products/products/40ml-vintage-octagonal-cups-goldfish-1"
               :price "$88.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-311_medium@2x.jpg?v=1532409516"
               :title "40ml Vintage Octagonal Cups - Bats"
               :url   "https://essenceoftea.com/collections/new-products/products/40ml-vintage-octagonal-cups-bats"
               :price nil}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-310_medium@2x.jpg?v=1532408618"
               :title "40ml Vintage Octagonal Cups - Flowers"
               :url   "https://essenceoftea.com/collections/new-products/products/40ml-vintage-octagonal-cups-flowers"
               :price nil}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-309_medium@2x.jpg?v=1532330082"
               :title "180ml Private Order Yixing Teapot"
               :url   "https://essenceoftea.com/collections/new-products/products/180ml-private-order-yixing-teapot-1"
               :price "$220.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-308_medium@2x.jpg?v=1532329554"
               :title "120ml Private Order Yixing Teapot"
               :url   "https://essenceoftea.com/collections/new-products/products/120ml-private-order-yixing-teapot"
               :price nil}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-307_medium@2x.jpg?v=1532328776"
               :title "150ml Private Order Yixing Teapot"
               :url   "https://essenceoftea.com/collections/new-products/products/150ml-private-order-yixing-teapot"
               :price "$220.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-306_medium@2x.jpg?v=1532328054"
               :title "120ml Private Order Yixing Teapot"
               :url   "https://essenceoftea.com/collections/new-products/products/1210ml-private-order-yixing-teapot"
               :price nil}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-305_medium@2x.jpg?v=1532068947"
               :title "140ml Private Order Yixing Teapot"
               :url   "https://essenceoftea.com/collections/new-products/products/140ml-private-order-yixing-teapot-1"
               :price nil}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-304_medium@2x.jpg?v=1532068642"
               :title "180ml Private Order Yixing Teapot"
               :url   "https://essenceoftea.com/collections/new-products/products/180ml-private-order-yixing-teapot"
               :price nil}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-303_medium@2x.jpg?v=1532057472"
               :title "115ml Private Order Yixing Teapot"
               :url   "https://essenceoftea.com/collections/new-products/products/115ml-private-order-yixing-teapot-2"
               :price "$220.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-302_medium@2x.jpg?v=1532057252"
               :title "180ml Private Order Yixing Teapot"
               :url   "https://essenceoftea.com/collections/new-products/products/170ml-private-order-yixing-teapot-2"
               :price "$220.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-301_medium@2x.jpg?v=1532056251"
               :title "160ml Private Order Yixing Teapot"
               :url   "https://essenceoftea.com/collections/new-products/products/170ml-private-order-yixing-teapot-1"
               :price "$220.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-300_medium@2x.jpg?v=1532051710"
               :title "170ml Private Order Yixing Teapot"
               :url   "https://essenceoftea.com/collections/new-products/products/170ml-private-order-yixing-teapot"
               :price nil}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-276_medium@2x.jpg?v=1531983158"
               :title "1950's RenXiDingGong Liu Bao"
               :url   "https://essenceoftea.com/collections/new-products/products/1950s-renxidinggong-liu-bao"
               :price "$130.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-221_1_medium@2x.jpg?v=1531198495"
               :title "1950's Da Xin Hang Liu Bao tea"
               :url   "https://essenceoftea.com/collections/new-products/products/1950s-da-xin-hang-liu-bao-tea"
               :price "$110.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-250_1_medium@2x.jpg?v=1531197863"
               :title "1990's Duoteli 1st Grade Liu Bao"
               :url   "https://essenceoftea.com/collections/new-products/products/1990s-duoteli-1st-grade-liu-bao"
               :price "$14.25"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/2-140_1_medium@2x.jpg?v=1531197331"
               :title "Lao Shou Xin"
               :url   "https://essenceoftea.com/collections/new-products/products/lao-shou-xin"
               :price "$60.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/2-132_1_0954374a-ceec-480a-8ab5-cf9d8f4246b0_medium@2x.jpg?v=1531196839"
               :title "Lao Cong Shui Xian - Liu Guan Zhai"
               :url   "https://essenceoftea.com/collections/new-products/products/lao-cong-shui-xian-liu-guan-zhai"
               :price "$13.95"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-45_1_medium@2x.jpg?v=1530503781"
               :title "1970's SSHC Liu Bao"
               :url   "https://essenceoftea.com/collections/new-products/products/1970s-sshc-liu-bao"
               :price "$30.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-268_medium@2x.jpg?v=1530412992"
               :title "1940's Sun Yi Shun Liu An 10g"
               :url   "https://essenceoftea.com/collections/new-products/products/1940s-sun-yi-shun-liu-an-10g"
               :price "$186.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-267_medium@2x.jpg?v=1530412274"
               :title "2014 EoT Long Lan Xu Puerh tea"
               :url   "https://essenceoftea.com/collections/new-products/products/2014-eot-long-lan-xu-puerh-tea"
               :price "$8.30"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/3-149_medium@2x.jpg?v=1530248088"
               :title "2018 Spring Secret Forest Puerh"
               :url   "https://essenceoftea.com/collections/new-products/products/2018-spring-secret-forest-puerh"
               :price "$10.50"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-265_medium@2x.jpg?v=1529809585"
               :title "1980's Menghai 7542 10g"
               :url   "https://essenceoftea.com/collections/new-products/products/1980s-menghai-7542-10g"
               :price "$185.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-254_medium@2x.jpg?v=1529641196"
               :title "1980's Thick Paper 8582 10g"
               :url   "https://essenceoftea.com/collections/new-products/products/1980s-thick-paper-8582"
               :price "$360.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-252_c879fb05-880b-4cd0-bc06-51205e6b228e_medium@2x.jpg?v=1529562507"
               :title "2008 Qianjiazhai Wild (Malaysia Stored)"
               :url   "https://essenceoftea.com/collections/new-products/products/2008-qianjiazhai-wild-malaysia-stored"
               :price "$140.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1vintage_porcelain_cups-5_medium@2x.jpg?v=1529558378"
               :title "25ml Vintage Octagonal Cups - Bagua"
               :url   "https://essenceoftea.com/collections/new-products/products/25ml-vintage-octagonal-cups-bagua"
               :price nil}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1vintage_porcelain_cups-4_medium@2x.jpg?v=1529555891"
               :title "40ml Vintage Octagonal Cups - Blue & White"
               :url   "https://essenceoftea.com/collections/new-products/products/40ml-vintage-octagonal-cups-blue-white"
               :price "$132.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1vintage_porcelain_cups-2_medium@2x.jpg?v=1529550471"
               :title "40ml Vintage Octagonal Cups - Orchids"
               :url   "https://essenceoftea.com/collections/new-products/products/40ml-vintage-octagonal-cups-orchids"
               :price nil}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1vintage_porcelain_cups_medium@2x.jpg?v=1529549810"
               :title "40ml Vintage Octagonal Cups - Butterflies"
               :url   "https://essenceoftea.com/collections/new-products/products/40ml-vintage-octagonal-cups-butterflies"
               :price nil}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-13_medium@2x.jpg?v=1529479922"
               :title "115ml Private Order Yixing Teapot"
               :url   "https://essenceoftea.com/collections/new-products/products/115ml-private-order-yixing-teapot-1"
               :price nil}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-12_medium@2x.jpg?v=1529479477"
               :title "100ml Private Order Yixing Teapot"
               :url   "https://essenceoftea.com/collections/new-products/products/100ml-private-order-yixing-teapot-1"
               :price nil}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-11_medium@2x.jpg?v=1529479086"
               :title "105ml Private Order Yixing Teapot"
               :url   "https://essenceoftea.com/collections/new-products/products/copy-of-105ml-private-order-yixing-teapot",
               :price nil}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/2-8_medium@2x.jpg?v=1529477970"
               :title "105ml Private Order Yixing Teapot"
               :url   "https://essenceoftea.com/collections/new-products/products/105ml-private-order-yixing-teapot-1",
               :price nil}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-9_medium@2x.jpg?v=1529477668"
               :title "95ml Private Order Yixing Teapot"
               :url   "https://essenceoftea.com/collections/new-products/products/95ml-private-order-yixing-teapot"
               :price "$220.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-8_medium@2x.jpg?v=1529477044"
               :title "100ml Private Order Yixing Teapot"
               :url   "https://essenceoftea.com/collections/new-products/products/100ml-private-order-yixing-teapot"
               :price "$220.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-7_medium@2x.jpg?v=1529476683"
               :title "105ml Private Order Yixing Teapot"
               :url   "https://essenceoftea.com/collections/new-products/products/105ml-private-order-yixing-teapot"
               :price "$220.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-6_medium@2x.jpg?v=1529473615"
               :title "80ml Private Order Yixing Teapot"
               :url   "https://essenceoftea.com/collections/new-products/products/80ml-private-order-yixing-teapot"
               :price "$220.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/Yixing_teapot_dicaoqing1-3_medium@2x.jpg?v=1529471418"
               :title "115ml Private Order Yixing Teapot"
               :url   "https://essenceoftea.com/collections/new-products/products/115ml-private-order-yixing-teapot"
               :price "$220.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/Yixing_teapot_dicaoqing1-2_medium@2x.jpg?v=1529468630"
               :title "85ml Private Order Yixing Teapot"
               :url   "https://essenceoftea.com/collections/new-products/products/85ml-private-order-yixing-teapot"
               :price nil}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/Yixing_teapot_dicaoqing2_medium@2x.jpg?v=1529467610"
               :title "140ml Private Order Yixing Teapot"
               :url   "https://essenceoftea.com/collections/new-products/products/140ml-private-order-yixing-teapot"
               :price "$220.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-3_medium@2x.jpg?v=1529465829"
               :title "150ml Dehua Vintage Fairness Cup/Gongdaobei"
               :url   "https://essenceoftea.com/collections/new-products/products/150ml-dehua-vintage-fairness-cup-gongdaobei"
               :price "$25.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/qing-dynasty-orchid-gaiwan1_medium@2x.jpg?v=1529462002"
               :title "140ml Antique Orchid Gaiwan"
               :url   "https://essenceoftea.com/collections/new-products/products/140ml-antique-orchid-gaiwan"
               :price nil}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/SF_wild_white_tea1_medium@2x.jpg?v=1528682637"
               :title "2018 Secret Forest Wild White Tea"
               :url   "https://essenceoftea.com/collections/new-products/products/2018-secret-forest-wild-white-tea"
               :price "$13.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1_d3e11d50-5c98-47cc-b6f2-9bfac4be1049_medium@2x.jpg?v=1528681467"
               :title "Vintage Dehua Porcelain Teacups"
               :url   "https://essenceoftea.com/collections/new-products/products/vintage-dehua-porcelain-teacups"
               :price nil}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/silver-rimmed-dehua-cups1_medium@2x.jpg?v=1528680686"
               :title "Silver-rimmed Dehua Antique Cups"
               :url   "https://essenceoftea.com/collections/new-products/products/silver-rimmed-dehua-antique-cups"
               :price nil}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/2000-green-peacock1_medium@2x.jpg?v=1528295011"
               :title "2000 Green Peacock Sheng Puerh"
               :url   "https://essenceoftea.com/collections/new-products/products/2000-green-peacock-sheng-puerh"
               :price "$15.75"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/2-143_medium@2x.jpg?v=1528294779"
               :title "2000 Camphor Aroma Sheng Puerh"
               :url   "https://essenceoftea.com/collections/new-products/products/2000-camphor-aroma-sheng-puerh"
               :price "$15.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1_medium@2x.jpg?v=1528612845"
               :title "2018 Spring Guafengzhai"
               :url   "https://essenceoftea.com/collections/new-products/products/2018-spring-guafengzhai"
               :price nil}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/dscf2291_1_medium@2x.jpg?v=1528287654"
               :title "950ml Yixing Zisha Kettle"
               :url   "https://essenceoftea.com/collections/new-products/products/950ml-yixing-zisha-kettle"
               :price "$210.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-327_1_medium@2x.jpg?v=1528286307"
               :title "2018 Spring Da Xue Shan Wild Red Tea"
               :url   "https://essenceoftea.com/collections/new-products/products/2018-spring-da-xue-shan-wild-red-tea"
               :price "$8.50"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/putiangongqing1_medium@2x.jpg?v=1528138057"
               :title "1950's Pu Tian Gong Qing Liu bao"
               :url   "https://essenceoftea.com/collections/new-products/products/1950s-pu-tian-gong-qing-liu-bao"
               :price "$150.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-445_medium@2x.jpg?v=1528136623"
               :title "2008 Shuang Long Bingdao"
               :url   "https://essenceoftea.com/collections/new-products/products/2008-shuang-long-bingdao"
               :price "$21.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1-192_medium@2x.jpg?v=1528134712"
               :title "2018 Spring \"Beyond the Clouds\""
               :url   "https://essenceoftea.com/collections/new-products/products/2018-spring-beyond-the-clouds"
               :price "$4.50"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/eot00277_1_medium@2x.jpg?v=1528133904"
               :title "1997 \"Horse Head Cliff\" Shui Xian Wuyi Yancha"
               :url   "https://essenceoftea.com/collections/new-products/products/1997-horse-head-cliff-shui-xian-wuyi-yancha"
               :price "$35.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/1992-dayeloose1-2_medium@2x.jpg?v=1528114908"
               :title "1992 Loose Leaf Puerh"
               :url   "https://essenceoftea.com/collections/new-products/products/1992-loose-leaf-puerh"
               :price "$8.70"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/2012-baotang1_medium@2x.jpg?v=1528114482"
               :title "2012 EoT Baotang Puerh"
               :url   "https://essenceoftea.com/collections/new-products/products/2012-eot-baotang-puerh"
               :price "$8.50"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/2012-bulang1_medium@2x.jpg?v=1529636651"
               :title "2012 EoT Bulang"
               :url   "https://essenceoftea.com/collections/new-products/products/2012-eot-bulang"
               :price "$13.75"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/eot-3leaf-1_medium@2x.jpg?v=1526722562"
               :title "EoT 3-leaf Liubao"
               :url   "https://essenceoftea.com/collections/new-products/products/eot-3-leaf-liubao"
               :price "$5.40"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/2000-si-jin-quan1_medium@2x.jpg?v=1526719743"
               :title "2005 Four Gold Coins Liu Bao"
               :url   "https://essenceoftea.com/collections/new-products/products/2005-four-gold-coins-liu-bao"
               :price "$5.25"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/EOT00595_medium@2x.jpg?v=1528291482"
               :title "2018 Spring EoT 10 Year Anniversary Yiwu"
               :url   "https://essenceoftea.com/collections/new-products/products/2018-spring-eot-10-year-anniversary-puerh"
               :price "$40.00"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/EOT00604_medium@2x.jpg?v=1526719007"
               :title "2018 Spring \"Piercing the Illusion\" Puerh Tea"
               :url   "https://essenceoftea.com/collections/new-products/products/2018-spring-piercing-the-ilusion-puerh-tea"
               :price "$9.60"}
              {:image "https://cdn.shopify.com/s/files/1/0006/9186/3604/products/EOT00581_medium@2x.jpg?v=1526713465"
               :title "2018 Spring Gedeng Guoyoulin Puerh Tea"
               :url   "https://essenceoftea.com/collections/new-products/products/2018-spring-gedeng-guoyoulin-puerh-tea"
               :price "$18.75"}] page)))))
