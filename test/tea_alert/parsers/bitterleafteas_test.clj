(ns tea-alert.parsers.bitterleafteas-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [net.cgrand.enlive-html :as enlive]
            [tea-alert.parsers.bitterleafteas :refer [parse]]))

(defn absolute-path
  [file]
  (str (.getPath (io/resource ".")) "../test-resources/" file))

(deftest parse-test
  (testing "Correctly parses source page"
    (let [page (-> (absolute-path "bitterleafteas.html") (java.io.File.) (enlive/html-resource) (parse))]
      (is (= [{:image "http://www.bitterleafteas.com/wp-content/uploads/2017/03/zodiac-jianshui-purple-clay-whistle-teapet-19-300x300.jpg"
               :title "Zodiac Jianshui Purple Clay Whistle Teapets"
               :url   "http://www.bitterleafteas.com/shop/teaware/zodiac-jianshui-purple-clay-whistle-teapets"
               :price "$24.00"}
              {:image "http://www.bitterleafteas.com/wp-content/uploads/2017/03/poet-wood-fired-hand-painted-gongdaobei-7-300x300.jpg"
               :title "Horse & Poet Artist Series Wood Fired Gong Dao Bei"
               :url   "http://www.bitterleafteas.com/shop/teaware/fair-cup/horse-poet-artist-series-wood-fired-gong-dao-bei"
               :price "$225.00"}
              {:image "http://www.bitterleafteas.com/wp-content/uploads/2017/03/sunrise-wood-fired-hand-painted-gaiwan-9-300x300.jpg"
               :title "Sunrise Artist Series Wood Fired Gaiwan"
               :url   "http://www.bitterleafteas.com/shop/teaware/gaiwan/sunrise-artist-series-wood-fired-gaiwan"
               :price "$250.00"}
              {:image "http://www.bitterleafteas.com/wp-content/uploads/2017/03/rooster-wood-fired-hand-painted-teacup-8-300x300.jpg"
               :title "Rooster Artist Series Wood Fired Teacup"
               :url   "http://www.bitterleafteas.com/shop/teaware/cups/rooster-artist-series-wood-fired-teacup"
               :price "$195.00"}
              {:image "http://www.bitterleafteas.com/wp-content/uploads/2017/03/catnap-wood-fired-hand-painted-teacup-7-300x300.jpg"
               :title "Catnap Artist Series Wood Fired Teacup"
               :url   "http://www.bitterleafteas.com/shop/teaware/cups/catnap-artist-series-wood-fired-teacup"
               :price "$195.00"}
              {:image "http://www.bitterleafteas.com/wp-content/uploads/2017/03/nt-wood-fired-hand-painted-teacup-8-300x300.jpg"
               :title "National Treasure Artist Series Wood Fired Teacup"
               :url   "http://www.bitterleafteas.com/shop/teaware/cups/national-treasure-artist-series-wood-fired-teacup"
               :price "$195.00"}
              {:image "http://www.bitterleafteas.com/wp-content/uploads/2017/03/romeo-wood-fired-hand-painted-teacup-9-300x300.jpg"
               :title "Romeo & Juliet Artist Series Wood Fired Teacups"
               :url   "http://www.bitterleafteas.com/shop/teaware/cups/romeo-artist-series-wood-fired-teacup"
               :price "$195.00"}
              {:image "http://www.bitterleafteas.com/wp-content/uploads/2017/03/blush-wood-fired-hand-painted-teacup-8-300x300.jpg"
               :title "Blush Artist Series Wood Fired Teacup"
               :url   "http://www.bitterleafteas.com/shop/teaware/cups/blush-artist-series-wood-fired-teacup"
               :price "$195.00"}
              {:image "http://www.bitterleafteas.com/wp-content/uploads/2017/03/snow-deer-wood-fired-hand-painted-teacup-7-300x300.jpg"
               :title "Snow Deer Artist Series Wood Fired Teacup"
               :url   "http://www.bitterleafteas.com/shop/teaware/cups/snow-deer-artist-series-wood-fired-teacup"
               :price "$195.00"}
              {:image "http://www.bitterleafteas.com/wp-content/uploads/2017/03/swing-wood-fired-hand-painted-teacup-8-300x300.jpg"
               :title "Swing Artist Series Wood Fired Teacup"
               :url   "http://www.bitterleafteas.com/shop/teaware/cups/swing-artist-series-wood-fired-teacup"
               :price "$195.00"}
              {:image "http://www.bitterleafteas.com/wp-content/uploads/2017/03/cauldron-wood-fired-jianshui-teapot-b1-4-300x300.jpg"
               :title "Cauldron Wood Fired Jianshui Purple Clay Teapot (85-90ml)"
               :url   "http://www.bitterleafteas.com/shop/teaware/cauldron-wood-fired-jianshui-purple-clay-teapot-85-90ml"
               :price "$205.00"}
              {:image "http://www.bitterleafteas.com/wp-content/uploads/2017/03/fireball-wood-fired-jianshui-teapot-a1-4-300x300.jpg"
               :title "Fireball Wood Fired Jianshui Purple Clay Teapot (90-100ml)"
               :url   "http://www.bitterleafteas.com/shop/teaware/fireball-wood-fired-jianshui-purple-clay-teapot-90-100ml"
               :price "$195.00"}
              {:image "http://www.bitterleafteas.com/wp-content/uploads/2017/03/puer-tea-tray-3-300x300.jpg"
               :title "White Puer Tray"
               :url   "http://www.bitterleafteas.com/shop/teaware/white-puer-tray"
               :price "$6.00"}
              {:image "http://www.bitterleafteas.com/wp-content/uploads/2017/02/bodhi-leaf-tea-strainer-8-300x300.jpg"
               :title "Bodhi Leaf Tea Strainer (3)"
               :url   "http://www.bitterleafteas.com/shop/teaware/accessory/bodhi-leaf-tea-strainer"
               :price "$12.00"}
              {:image "http://www.bitterleafteas.com/wp-content/uploads/2017/02/teaware-sample-sale-0-300x300.jpg"
               :title "2017 Teaware Sample Sale"
               :url   "http://www.bitterleafteas.com/shop/teaware/2017-teaware-sample-sale"
               :price "$3.50"}
              {:image "http://www.bitterleafteas.com/wp-content/uploads/2017/02/bodhidharma-wood-fired-teapet5-300x300.jpg"
               :title "Bodhidharma Wood Fired Tea Pet"
               :url   "http://www.bitterleafteas.com/shop/teaware/bodhidharma-wood-fired-tea-pet"
               :price "$28.00"}
              {:image "http://www.bitterleafteas.com/wp-content/uploads/2017/01/wood-fired-jianshui-teapot-electra-9-300x300.jpg"
               :title "Wood Fired Jianshui Purple Clay Teacups – Electra"
               :url   "http://www.bitterleafteas.com/shop/teaware/jianshui-zitao/wood-fired-jianshui-purple-clay-teacups-electra"
               :price "$38.00"}
              {:image "http://www.bitterleafteas.com/wp-content/uploads/2017/01/bitterleaf-2016-tea-tree-flower-3-300x300.jpg"
               :title "Fair Lady 2016 Jing Mai Tea Tree Flower"
               :url   "http://www.bitterleafteas.com/shop/tea/non-puer/fair-lady-2016-jing-mai-tea-tree-flower"
               :price "$3.50"}
              {:image "http://www.bitterleafteas.com/wp-content/uploads/2017/01/bitterleaf-2016-jing-mai-crab-legs-1-300x300.jpg"
               :title "2016 Jing Mai Crab Legs"
               :url   "http://www.bitterleafteas.com/shop/tea/non-puer/2016-jing-mai-crab-legs"
               :price "$10.00"}
              {:image "http://www.bitterleafteas.com/wp-content/uploads/2017/01/bitterleaf-1998-aged-raw-puer-1-300x300.jpg"
               :title "Whatever 98 Private Collection Raw Puer"
               :url   "http://www.bitterleafteas.com/shop/tea/puer/raw-puer/whatever-98-private-collection-raw-puer"
               :price "$10.50"}] page)))))

