(ns tea-alert.parsers.chawangshop-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [net.cgrand.enlive-html :as enlive]
            [tea-alert.parsers.chawangshop :refer [parse]]))

(defn absolute-path
  [file]
  (str (.getPath (io/resource ".")) "../test-resources/" file))

(deftest parse-test
  (testing "Correctly parses source page"
    (let [page (-> (absolute-path "chawangshop.html") (java.io.File.) (enlive/html-resource) (parse))]
      (is (= [{:image "http://www.chawangshop.com/media/catalog/product/cache/1/small_image/135x/9df78eab33525d08d6e5fb8d27136e95/l/_/l.jpg"
               :title "Late 1990s Zini Shuiping pot 110cc"
               :url   "http://www.chawangshop.com/index.php/late-1990s-zini-shuiping-pot-110cc.html"
               :price "$78.00"}
              {:image "http://www.chawangshop.com/media/catalog/product/cache/1/small_image/135x/9df78eab33525d08d6e5fb8d27136e95/s/_/s.jpg"
               :title "Late 1990s Zini Shuiping pot 65cc"
               :url   "http://www.chawangshop.com/index.php/late-1990s-zini-shuiping-pot-65cc.html"
               :price "$65.00"}
              {:image "http://www.chawangshop.com/media/catalog/product/cache/1/small_image/135x/9df78eab33525d08d6e5fb8d27136e95/m/_/m.jpg"
               :title "Late 1990s Zini Shuiping pot 85cc"
               :url   "http://www.chawangshop.com/index.php/late-1990s-zini-shuiping-pot-85cc.html"
               :price "$70.00"}
              {:image "http://www.chawangshop.com/media/catalog/product/cache/1/small_image/135x/9df78eab33525d08d6e5fb8d27136e95/c/r/cracked_ice_pattern_porcelain_tea_caddy.jpg"
               :title "Cracked Ice Pattern Porcelain Tea Caddy 10cm"
               :url   "http://www.chawangshop.com/index.php/cracked-ice-pattern-porcelain-tea-caddy-10cm.html"
               :price "$9.00"}
              {:image "http://www.chawangshop.com/media/catalog/product/cache/1/small_image/135x/9df78eab33525d08d6e5fb8d27136e95/c/h/chan.jpg"
               :title "Nixing Teapot \"Plum Blossom and Chan\" 100ml"
               :url   "http://www.chawangshop.com/index.php/nixing-teapot-plum-blossom-and-chan-100ml.html"
               :price "$125.00"}
              {:image "http://www.chawangshop.com/media/catalog/product/cache/1/small_image/135x/9df78eab33525d08d6e5fb8d27136e95/m/e/meizhu.jpg"
               :title "Nixing Teapot \"Plum blossom and Bamboo\" 100ml"
               :url   "http://www.chawangshop.com/index.php/nixing-teapot-plum-blossom-and-bamboo-100ml.html"
               :price "$125.00"}
              {:image "http://www.chawangshop.com/media/catalog/product/cache/1/small_image/135x/9df78eab33525d08d6e5fb8d27136e95/l/o/loofah.jpg"
               :title "Loofah Mat 8cm"
               :url   "http://www.chawangshop.com/index.php/loofah-mat-8cm.html"
               :price "$1.50"}
              {:image "http://www.chawangshop.com/media/catalog/product/cache/1/small_image/135x/9df78eab33525d08d6e5fb8d27136e95/p/a/panhu.jpg"
               :title "Nixing Teapot \"Pan Hu\" 100ml"
               :url   "http://www.chawangshop.com/index.php/nixing-teapot-pan-hu.html"
               :price "$85.00"}
              {:image "http://www.chawangshop.com/media/catalog/product/cache/1/small_image/135x/9df78eab33525d08d6e5fb8d27136e95/f/a/fanggu.jpg"
               :title "Nixing Teapot \"Fang Gu\" 85ml"
               :url   "http://www.chawangshop.com/index.php/nixing-teapot-fang-gu-85ml.html"
               :price "$85.00"}
              {:image "http://www.chawangshop.com/media/catalog/product/cache/1/small_image/135x/9df78eab33525d08d6e5fb8d27136e95/s/h/shoumei.jpg"
               :title "2011 Fujian Zhenghe Shoumei White Tea 200g"
               :url   "http://www.chawangshop.com/index.php/2011-fujian-zhenghe-shoumei-white-tea-200g.html"
               :price "$18.00"}
              {:image "http://www.chawangshop.com/media/catalog/product/cache/1/small_image/135x/9df78eab33525d08d6e5fb8d27136e95/1/_/1_48_1.jpg"
               :title "2016 Chawangpu Manzhuan Gushu Puerh Tea 200g"
               :url   "http://www.chawangshop.com/index.php/2016-chawangpu-manzhuan-gushu-raw-puerh-cake.html"
               :price "$65.00"}
              {:image "http://www.chawangshop.com/media/catalog/product/cache/1/small_image/135x/9df78eab33525d08d6e5fb8d27136e95/z/h/zhenshang.jpg"
               :title "2016 Chawangpu Jinggu Zhen Shang Gushu Puerh Tea 200g"
               :url   "http://www.chawangshop.com/index.php/2016-chawangpu-jinggu-zhen-shang-gushu-puerh-cake-200g.html"
               :price "$55.00"}
              {:image "http://www.chawangshop.com/media/catalog/product/cache/1/small_image/135x/9df78eab33525d08d6e5fb8d27136e95/y/o/youshang.jpg"
               :title "2016 Chawangpu Jinggu You Shang Gushu Puerh Tea 200g"
               :url   "http://www.chawangshop.com/index.php/2016-chawangpu-jinggu-you-shang-gushu-puerh-cake-200g.html"
               :price "$65.00"}
              {:image "http://www.chawangshop.com/media/catalog/product/cache/1/small_image/135x/9df78eab33525d08d6e5fb8d27136e95/1/_/1_52_4.jpg"
               :title "2016 Chawangpu Bada Laoyu Raw Puerh Cake 400g"
               :url   "http://www.chawangshop.com/index.php/2016-chawangpu-bada-laoyu-400g.html"
               :price "$25.00"}
              {:image "http://www.chawangshop.com/media/catalog/product/cache/1/small_image/135x/9df78eab33525d08d6e5fb8d27136e95/h/e/hekai_1_1.jpg"
               :title "2016 Chawangpu Hekai Gushu Puerh Tea 200g"
               :url   "http://www.chawangshop.com/index.php/2016-chawangpu-hekai-gushu-xiao-bing-200g.html"
               :price "$38.00"}
              {:image "http://www.chawangshop.com/media/catalog/product/cache/1/small_image/135x/9df78eab33525d08d6e5fb8d27136e95/1/_/1_52_3.jpg"
               :title "2016 Chawangpu Bulang Shan Raw Puerh Tea 200g"
               :url   "http://www.chawangshop.com/index.php/2016-chawangpu-bulang-shan-raw-puerh-cake-200g.html"
               :price "$22.00"}
              {:image "http://www.chawangshop.com/media/catalog/product/cache/1/small_image/135x/9df78eab33525d08d6e5fb8d27136e95/1/_/1_52_1.jpg"
               :title "2016 Chawangpu Menghai \"Xiao Yao\" Puerh Tea 400g"
               :url   "http://www.chawangshop.com/index.php/2016-chawangpu-menghai-xiao-yao-raw-puerh-cake-400g.html"
               :price "$90.00"}
              {:image "http://www.chawangshop.com/media/catalog/product/cache/1/small_image/135x/9df78eab33525d08d6e5fb8d27136e95/1/_/1_48.jpg"
               :title "2016 Chawangpu Bada Puerh Tea 200g"
               :url   "http://www.chawangshop.com/index.php/2016-chawangpu-bada-raw-puer-cake-200g.html"
               :price "$20.00"}
              {:image "http://www.chawangshop.com/media/catalog/product/cache/1/small_image/135x/9df78eab33525d08d6e5fb8d27136e95/1/_/1_51.jpg"
               :title "2016 Chawangpu Mengsong Puerh Tea 200g"
               :url   "http://www.chawangshop.com/index.php/2016-chawangpu-mengsong-old-tree-xiao-bing-200g.html"
               :price "$24.00"}
              {:image "http://www.chawangshop.com/media/catalog/product/cache/1/small_image/135x/9df78eab33525d08d6e5fb8d27136e95/1/_/1_52.jpg"
               :title "2016 Chawangpu Nannuo Shan Gushu Puerh Tea 200g"
               :url   "http://www.chawangshop.com/index.php/2016-chawangpu-nannuo-shan-gushu-raw-puerh-cake-200g.html"
               :price "$65.00"}
              {:image "http://www.chawangshop.com/media/catalog/product/cache/1/small_image/135x/9df78eab33525d08d6e5fb8d27136e95/2/_/2_23_5.jpg"
               :title "2016 Chawangpu Manmai Selected Gushu Puerh Tea 200g"
               :url   "http://www.chawangshop.com/index.php/2016-chawangpu-manmai-gushu-selected-tree.html"
               :price "$55.00"}
              {:image "http://www.chawangshop.com/media/catalog/product/cache/1/small_image/135x/9df78eab33525d08d6e5fb8d27136e95/1/_/1_47_11.jpg"
               :title "2016 Chawangpu Manmai Gushu Puerh Tea 200g"
               :url   "http://www.chawangshop.com/index.php/2016-chawangpu-manmai-gushu-200g.html"
               :price "$40.00"}] page)))))
