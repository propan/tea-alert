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
      (is (= [{:image "http://white2tea.com/puer/wp-content/uploads/2017/07/2017-tuhao-as-fuck-raw-puerh-tea-1-350x300.jpg"
               :title "2017 Tuhao af"
               :url   "http://white2tea.com/product/2017-tuhao-as-fuck/"
               :price "$19.50"}
              {:image "http://white2tea.com/puer/wp-content/uploads/2017/07/2017-manichee-raw-puer-tea-1-350x300.jpg"
               :title "2017 Manichee"
               :url   "http://white2tea.com/product/2017-manichee/"
               :price "$11.00"}
              {:image "http://white2tea.com/puer/wp-content/uploads/2017/07/2017-dangerous-messengers-puerh-tea-1-350x300.jpg"
               :title "2017 Dangerous Messengers"
               :url   "http://white2tea.com/product/2017-dangerous-messengers/"
               :price "$8.50"}
              {:image "http://white2tea.com/puer/wp-content/uploads/2017/07/2017-pussy-sheng-puer-tea-1-350x300.jpg"
               :title "2017 Pussy"
               :url   "http://white2tea.com/product/2017-pussy/"
               :price "$12.50"}
              {:image "http://white2tea.com/puer/wp-content/uploads/2017/07/2017-fuck-what-u-heard-tea-350x300.jpg"
               :title "2017 fuck what u heard"
               :url   "http://white2tea.com/product/2017-fuck-what-u-heard/"
               :price "$27.00"}
              {:image "http://white2tea.com/puer/wp-content/uploads/2017/07/2017-shes-not-me-puer-tea-350x300.jpg"
               :title "2017 She’s Not Me"
               :url   "http://white2tea.com/product/2017-shes-not-me/"
               :price "$17.00"}
              {:image "http://white2tea.com/puer/wp-content/uploads/2017/06/2017-U2CANHELP-charity-tea-1-350x300.jpg"
               :title "2017 U 2 CAN HELP"
               :url   "http://white2tea.com/product/2017-u-2-can-help/"
               :price "$49.00"}
              {:image "http://white2tea.com/puer/wp-content/uploads/2016/11/2016-nightlife-moonlight-white-tea-1-350x300.jpg"
               :title "2017 Nightlife"
               :url   "http://white2tea.com/product/2017-nightlife/"
               :price "$4.70"}
              {:image "http://white2tea.com/puer/wp-content/uploads/2017/05/2002-Yiwu-Huangpian-Brick-350x300.jpg"
               :title "2002 Yiwu Huangpian Raw Puer Brick"
               :url   "http://white2tea.com/product/2002-yiwu-huangpian-raw-puer-brick/"
               :price "$7.00"}
              {:image "http://white2tea.com/puer/wp-content/uploads/2017/05/Ice-Green-Fujian-Greentea-tea-1-350x300.jpg"
               :title "Ice Green Fujian"
               :url   "http://white2tea.com/product/ice-green-fujian/"
               :price "$8.75"}
              {:image "http://white2tea.com/puer/wp-content/uploads/2017/03/2017-old-reliable-ripe-puer-tea-white2tea-1-350x300.jpg"
               :title "2017 Old Reliable"
               :url   "http://white2tea.com/product/2017-old-reliable/"
               :price "$1.95"}
              {:image "http://white2tea.com/puer/wp-content/uploads/2017/03/2017-grandpas-ripe-balls-puer-tea-350x300.jpg"
               :title "2017 Grandpa’s"
               :url   "http://white2tea.com/product/2017-grandpas/"
               :price "$0.75"}] page)))))
