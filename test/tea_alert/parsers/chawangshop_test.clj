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
      (is (= [{:image "https://www.chawangshop.com/media/catalog/product/cache/small_image/240x300/beff4985b56e3afdbeabfc89641a4582/2/0/2014_ming_sheng_hao_bulang_arbor_ripe_puerh_tea_357g.jpg"
               :title "2014 Ming Sheng Hao Bulang Arbor Ripe Puerh Tea 357g"
               :url   "https://www.chawangshop.com/2014-ming-sheng-hao-bulang-arbor-ripe-puerh-tea.html"
               :price "$25.00"}
              {:image "https://www.chawangshop.com/media/catalog/product/cache/small_image/240x300/beff4985b56e3afdbeabfc89641a4582/6/5/650_1.jpg"
               :title "Chaozhou Red Clay Pot 650ml"
               :url   "https://www.chawangshop.com/chaozhou-red-clay-pot-650ml.html"
               :price "$50.00"}
              {:image "https://www.chawangshop.com/media/catalog/product/cache/small_image/240x300/beff4985b56e3afdbeabfc89641a4582/r/i/rice_grain.jpg"
               :title "Jingdezhen Vintage Tea Cup \"Rice Grain Pattern\" with colored decoration 50ml"
               :url   "https://www.chawangshop.com/jingdezhen-vintage-tea-cup-rice-grain-pattern-with-colored-decoration-50ml.html"
               :price "$3.50"}
              {:image "https://www.chawangshop.com/media/catalog/product/cache/small_image/240x300/beff4985b56e3afdbeabfc89641a4582/l/i/linglong_1.jpg"
               :title "Jingdezhen Vintage Tea Cup \"White Ling Long\" 50ml"
               :url   "https://www.chawangshop.com/jingdezhen-vintage-tea-cup-white-ling-long-50ml.html"
               :price "$3.00"}
              {:image "https://www.chawangshop.com/media/catalog/product/cache/small_image/240x300/beff4985b56e3afdbeabfc89641a4582/e/_/e.jpg"
               :title "Vintage Blue-and-white plate \"Entangled Floral Branch\" E"
               :url   "https://www.chawangshop.com/vintage-blue-and-white-plate-entangled-floral-branch-e.html"
               :price "$20.00"}
              {:image "https://www.chawangshop.com/media/catalog/product/cache/small_image/240x300/beff4985b56e3afdbeabfc89641a4582/p/l/plate.jpg"
               :title "Vintage Blue and White Plate"
               :url   "https://www.chawangshop.com/vintage-blue-and-white-plate.html"
               :price "$20.00"}
              {:image "https://www.chawangshop.com/media/catalog/product/cache/small_image/240x300/beff4985b56e3afdbeabfc89641a4582/g/a/ganoderma.jpg"
               :title "Vintage Blue-and-white plate “Ganoderma”"
               :url   "https://www.chawangshop.com/vintage-blue-and-white-plate-ganoderma.html"
               :price "$20.00"}
              {:image "https://www.chawangshop.com/media/catalog/product/cache/small_image/240x300/beff4985b56e3afdbeabfc89641a4582/f/i/file_13_16.jpg"
               :title "Bamboo tea tray “Xiao Kong Ming” (Holding Water) 40.3×22×6CM"
               :url   "https://www.chawangshop.com/bamboo-tea-tray-xiao-kong-ming.html"
               :price "$25.00"}] page)))))
