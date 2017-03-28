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
      (is (= [{:image "http://white2tea.com/puer/wp-content/uploads/2017/01/Butter-Flower-Oolong-1-2-350x300.jpg"
               :title "Butter Flower"
               :url   "http://white2tea.com/product/butter-flower/"
               :price "$24.50"}
              {:image "http://white2tea.com/puer/wp-content/uploads/2017/01/milan-dancong-Oolong-1-350x300.jpg"
               :title "Milan Dancong"
               :url   "http://white2tea.com/product/milan-dancong/"
               :price "$24.50"}
              {:image "http://white2tea.com/puer/wp-content/uploads/2017/01/shuixian-Oolong-1-350x300.jpg"
               :title "Shui Xian"
               :url   "http://white2tea.com/product/shui-xian/"
               :price "$14.50"}
              {:image "http://white2tea.com/puer/wp-content/uploads/2016/11/2016-nightlife-moonlight-white-tea-1-350x300.jpg"
               :title "2016 Nightlife"
               :url   "http://white2tea.com/product/2016-nightlife/"
               :price "$4.70"}
              {:image "http://white2tea.com/puer/wp-content/uploads/2016/11/2016-post-truth-raw-Puerh-tea-1-350x300.jpg"
               :title "2016 post truth"
               :url   "http://white2tea.com/product/2016-post-truth/"
               :price "$6.50"}
              {:image "http://white2tea.com/puer/wp-content/uploads/2016/11/2016-Chocobar-tea-sampleset-teatasting-1-350x300.jpg"
               :title "ChocoBar Tasting Set"
               :url   "http://white2tea.com/product/chocobar-tasting-set/"
               :price "$55.00"}
              {:image "http://white2tea.com/puer/wp-content/uploads/2016/11/2016-Chocobar-tea-raw-Puer-c-350x300.jpg"
               :title "2016 ChocoBrick Raw Puer"
               :url   "http://white2tea.com/product/2016-chocobrick-raw-puer/"
               :price "$14.00"}
              {:image "http://white2tea.com/puer/wp-content/uploads/2016/11/2016-Chocobar-tea-blacktea-c-350x300.jpg"
               :title "2016 ChocoBrick Black Tea"
               :url   "http://white2tea.com/product/2016-chocobrick-black-tea/"
               :price "$15.00"}
              {:image "http://white2tea.com/puer/wp-content/uploads/2016/11/2016-Chocobar-tea-whitetea-c-350x300.jpg"
               :title "2016 ChocoBrick White Tea"
               :url   "http://white2tea.com/product/2016-chocobrick-white-tea/"
               :price "$15.00"}
              {:image "http://white2tea.com/puer/wp-content/uploads/2016/11/2016-Chocobar-tea-ripe-Puer-tea-c-350x300.jpg"
               :title "2016 ChocoBrick Ripe Puer"
               :url   "http://white2tea.com/product/2016-chocobrick-ripe-puer/"
               :price "$11.00"}
              {:image "http://white2tea.com/puer/wp-content/uploads/2016/11/2013-baimudan-whitetea-350x300.jpg"
               :title "2013 Baimudan"
               :url   "http://white2tea.com/product/2013-baimudan/"
               :price "$14.50"}
              {:image "http://white2tea.com/puer/wp-content/uploads/2016/11/2014-shoumei-350x300.jpg"
               :title "2014 Shoumei"
               :url   "http://white2tea.com/product/2014-shoumei/"
               :price "$9.50"}
              {:image "http://white2tea.com/puer/wp-content/uploads/2016/11/2015-Channel-Orange-Ripe-Puerh-tea-350x300.jpg"
               :title "2015 Channel Orange"
               :url   "http://white2tea.com/product/2015-channel-orange/"
               :price "$22.00"}
              {:image "http://white2tea.com/puer/wp-content/uploads/2016/11/2016-trap-bird-shou-Puerh-tea-1-350x300.jpg"
               :title "2016 Trap Bird"
               :url   "http://white2tea.com/product/2016-trap-bird/"
               :price "$2.90"}
              {:image "http://white2tea.com/puer/wp-content/uploads/2016/11/2016-Sister-Brother-sampleset-tea-b-350x300.jpg"
               :title "2016 Sister Brother"
               :url   "http://white2tea.com/product/2016-sister-brother/"
               :price "$49.00"}
              {:image "http://white2tea.com/puer/wp-content/uploads/2016/11/2016-Brown-Sugar-Ripe-Puerh-tea-b-350x300.jpg"
               :title "2016 Brown Sugar"
               :url   "http://white2tea.com/product/2016-brown-sugar/"
               :price "$2.50"}] page)))))
