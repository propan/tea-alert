(ns tea-alert.parsers.yunnansourcing-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [net.cgrand.enlive-html :as enlive]
            [tea-alert.parsers.yunnansourcing :refer [parse]]))

(defn absolute-path
  [file]
  (str (.getPath (io/resource ".")) "../test-resources/" file))

(deftest parse-test
  (testing "Correctly parses source page"
    (let [page (-> (absolute-path "yunnansourcing.html") (java.io.File.) (enlive/html-resource) (parse))]
      (is (= [{:image "http://cdn.shopify.com/s/files/1/0586/9817/products/2009_lbz_250g_bing_large.jpg?v=1491577296"
               :title "2009 Lao Ban Zhang Premium Raw Pu-erh tea cake * 250g"
               :url   "https://yunnansourcing.com/collections/new-products/products/2009-lao-ban-zhang-premium-raw-pu-erh-tea-cake-250g"
               :price "$220.00"}
              {:image "http://cdn.shopify.com/s/files/1/0586/9817/products/2006_fall_lao_banzhang_bing_large.jpg?v=1491577302"
               :title "2006 Fall Lao Ban Zhang Raw Pu-erh Tea cake * 400g"
               :url   "https://yunnansourcing.com/collections/new-products/products/2006-fall-lao-ban-zhang-raw-pu-erh-tea-cake-400g"
               :price "$400.00"}
              {:image "http://cdn.shopify.com/s/files/1/0586/9817/products/2005_banzhang_brick_large.jpg?v=1491541490"
               :title "2007 Mengku Lao Ban Zhang Pu-erh Tea mini Brick * 100g"
               :url   "https://yunnansourcing.com/collections/new-products/products/2007-mengku-lao-ban-zhang-pu-erh-tea-mini-brick-100g"
               :price "$62.00"}
              {:image "http://cdn.shopify.com/s/files/1/0586/9817/products/2790-4066-thickbox_large.jpg?v=1491411516"
               :title "Pu-erh Tea brick Gifting / Storage Box for 250 gram bricks * Old Pu-erh * Folding Box and Bag"
               :url   "https://yunnansourcing.com/collections/new-products/products/pu-erh-tea-brick-gifting-storage-box-for-250-gram-bricks-old-pu-erh-folding-box-and-bag"
               :price "$2.50"}
              {:image "http://cdn.shopify.com/s/files/1/0586/9817/products/2789-4061-thickbox_large.jpg?v=1491411211"
               :title "Pu-erh Tea cake Gifting / Storage Box for 357 - 400 gram cakes * Yunnan Impression * Folding Box and Bag"
               :url   "https://yunnansourcing.com/collections/new-products/products/pu-erh-tea-cake-gifting-storage-box-for-357-400-gram-cakes-yunnan-impression-folding-box-and-bag"
               :price "$2.50"}
              {:image "http://cdn.shopify.com/s/files/1/0586/9817/products/2788-4058-thickbox_large.jpg?v=1491410419"
               :title "Pu-erh Tea cake Gifting / Storage Box for 357 - 400 gram cakes * Classic Pu-erh * Folding Box and Bag"
               :url   "https://yunnansourcing.com/collections/new-products/products/pu-erh-tea-cake-gifting-storage-box-for-357-400-gram-cakes-classic-pu-erh-folding-box-and-bag"
               :price "$2.50"}
              {:image "http://cdn.shopify.com/s/files/1/0586/9817/products/2750-4044-thickbox_a77be013-dd89-47c5-a899-e81fddc33574_large.jpg?v=1491410148"
               :title "Cloth Sack for Storing and Carrying Seven 357-400 gram Pu-erh tea cakes"
               :url   "https://yunnansourcing.com/collections/new-products/products/cloth-sack-for-storing-and-carrying-seven-357-400-gram-pu-erh-tea-cakes-1"
               :price "$2.00"}
              {:image "http://cdn.shopify.com/s/files/1/0586/9817/products/2660-3440-thickbox_large.jpg?v=1491409592"
               :title "Foil Pouches for Tea Storage * Heat Seal Open Mouth"
               :url   "https://yunnansourcing.com/collections/new-products/products/foil-pouches-for-tea-storage-heat-seal-open-mouth"
               :price "$8.00"}
              {:image "http://cdn.shopify.com/s/files/1/0586/9817/products/3344-7050-thickbox_large.jpg?v=1491408909"
               :title "Pu-erh Tea cake Gifting / Storage Box for 357 - 400 gram cakes * Qizi Bing"
               :url   "https://yunnansourcing.com/collections/new-products/products/pu-erh-tea-cake-gifting-storage-box-for-357-400-gram-cakes-qizi-bing"
               :price "$1.50"}
              {:image "http://cdn.shopify.com/s/files/1/0586/9817/products/bamboo_cake_box_1_large.jpg?v=1491408763"
               :title "Faux Bamboo Round Slip Cover Pu-erh Tea Cake Container"
               :url   "https://yunnansourcing.com/collections/new-products/products/faux-bamboo-round-slip-cover-pu-erh-tea-cake-container"
               :price "$7.50"}
              {:image "http://cdn.shopify.com/s/files/1/0586/9817/products/2014_imperial_dragon_well_1_65d8fbc7-4cbd-41b3-b8f0-92c2d2c47002_large.jpg?v=1491406574"
               :title "Imperial Dragon Well Tea From Hangzhou * Long Jing Tea 2017"
               :url   "https://yunnansourcing.com/collections/new-products/products/imperial-dragon-well-tea-from-hangzhou-long-jing-tea-2017"
               :price "$7.00"}
              {:image "http://cdn.shopify.com/s/files/1/0586/9817/products/silver_pouch_full_large.jpg?v=1491401330"
               :title "Heavy Duty Zip Lock Silver Stand-up Pouches for Tea Packaging and Storage"
               :url   "https://yunnansourcing.com/collections/new-products/products/heavy-duty-zip-lock-silver-stand-up-pouches-for-tea-packaging-and-storage"
               :price "$5.00"}
              {:image "http://cdn.shopify.com/s/files/1/0586/9817/products/2016_spring_fuding_baimudan_1_465f72b6-f15c-4829-909e-b2e532f5b19c_large.jpg?v=1491325635"
               :title "Jing Gu Yang Ta Yunnan Bai Mu Dan White tea * Spring 2017"
               :url   "https://yunnansourcing.com/collections/new-products/products/jing-gu-yang-ta-yunnan-bai-mu-dan-white-tea-spring-2017"
               :price "$4.50"}
              {:image "http://cdn.shopify.com/s/files/1/0586/9817/products/2016_fengqing_pearls_1_51dda062-37d4-457d-aaf3-8ecc11f8d0f5_large.jpg?v=1491325280"
               :title "Feng Qing Premium \"Black Gold Pearls\" Yunnan Black Tea * Spring 2017"
               :url   "https://yunnansourcing.com/collections/new-products/products/feng-qing-premium-black-gold-pearls-yunnan-black-tea-spring-2017"
               :price "$6.00"}
              {:image "http://cdn.shopify.com/s/files/1/0586/9817/products/2016_fengqing_yesheng_hong_ad8955d2-043b-495b-8b9a-6542d0841f28_large.jpg?v=1491325171"
               :title "Feng Qing Ye Sheng Hong Cha Wild Tree Purple Black tea * Spring 2017"
               :url   "https://yunnansourcing.com/collections/new-products/products/feng-qing-ye-sheng-hong-cha-wild-tree-purple-black-tea-spring-2017"
               :price "$9.00"}
              {:image "http://cdn.shopify.com/s/files/1/0586/9817/products/DSC_0127_large.JPG?v=1491332673"
               :title "Hand-Made \"Bos Taurus Horn\" Horse Hair Brush"
               :url   "https://yunnansourcing.com/collections/new-products/products/hand-made-bos-taurus-horn-horse-hair-brush"
               :price "$10.00"}
              {:image "http://cdn.shopify.com/s/files/1/0586/9817/products/square_8ff3455f-d5bf-493d-a6c7-4fe165d7aeb5_large.jpg?v=1491325109"
               :title "Hardwood Carved \"Bamboo\" Handle Damascus Steel Knife for Pu-erh Tea"
               :url   "https://yunnansourcing.com/collections/new-products/products/hardwood-carved-bamboo-handle-damascus-steel-knife-for-pu-erh-tea"
               :price "$36.00"}
              {:image "http://cdn.shopify.com/s/files/1/0586/9817/products/aquare_large.jpg?v=1491322492"
               :title "Deer Horn Handle Damascus Steel Knife for Pu-erh Tea"
               :url   "https://yunnansourcing.com/collections/new-products/products/bone-handle-damascus-steel-knife-for-pu-erh-tea"
               :price "$50.00"}
              {:image "http://cdn.shopify.com/s/files/1/0586/9817/products/square_3a9e0471-8f0b-4745-80ee-b1c78da3823a_large.jpg?v=1491321483"
               :title "Wenge Wood \"Dragon Pole\" Damascus Steel Knife for Pu-erh Tea"
               :url   "https://yunnansourcing.com/collections/new-products/products/wenge-wood-dragon-pole-damascus-steel-knife-for-pu-erh-tea"
               :price "$20.00"}
              {:image "http://cdn.shopify.com/s/files/1/0586/9817/products/DSC_0107_a300e238-ee2d-4c79-90da-1a2121347e79_large.JPG?v=1491317266"
               :title "Wenge Wood Pu-erh Tea Pick and Brush Combo"
               :url   "https://yunnansourcing.com/collections/new-products/products/wenge-wood-pu-erh-tea-pick-and-brush-combo"
               :price "$6.00"}
              {:image "http://cdn.shopify.com/s/files/1/0586/9817/products/squarish_198a5226-fe1e-4bca-b44d-f17d9929e456_large.jpg?v=1491279561"
               :title "Hand-Woven Wicker Styled Strainer for Gong Fu Tea"
               :url   "https://yunnansourcing.com/collections/new-products/products/hand-woven-wicker-styled-strainer-for-gong-fu-tea"
               :price "$7.00"}
              {:image "http://cdn.shopify.com/s/files/1/0586/9817/products/square_fbb007ff-af78-42d0-be3b-ca1222ce358e_large.jpg?v=1491279522"
               :title "Obscured Glass \"Tumbler\" Cha Hai for Gong Fu Tea Brewing"
               :url   "https://yunnansourcing.com/collections/new-products/products/obscured-glass-tumbler-cha-hai-for-gong-fu-tea-brewing"
               :price "$14.00"}
              {:image "http://cdn.shopify.com/s/files/1/0586/9817/products/square_0c396010-4206-478e-ba06-52b21860cb1a_large.jpg?v=1491279044"
               :title "Hand-Made \"Cream\" Jian Shui White Clay Cups * Set of 2"
               :url   "https://yunnansourcing.com/collections/new-products/products/hand-made-cream-jian-shui-white-clay-cups-set-of-2"
               :price "$12.00"}
              {:image "http://cdn.shopify.com/s/files/1/0586/9817/products/DSC_0244_1bfdaeac-ab9c-417d-bc05-2f27407f8144_large.JPG?v=1491278128"
               :title "Black Glazed \"Rain Drops\" Gaiwan for Gong Fu Tea Brewing"
               :url   "https://yunnansourcing.com/collections/new-products/products/black-glazed-rain-drops-gaiwan-for-gong-fu-tea-brewing"
               :price "$11.00"}] page)))))
