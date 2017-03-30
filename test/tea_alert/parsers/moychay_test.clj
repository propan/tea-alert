(ns tea-alert.parsers.moychay-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [net.cgrand.enlive-html :as enlive]
            [tea-alert.parsers.moychay :refer [parse]]))

(defn absolute-path
  [file]
  (str (.getPath (io/resource ".")) "../test-resources/" file))

(deftest parse-test
  (testing "Correctly parses source page"
    (let [page (-> (absolute-path "moychay.html") (java.io.File.) (enlive/html-resource) (parse))]
      (is (= [{:image "https://moychay.com/system/product_fotos/images/000/400/559/small/_MG_2472.jpg?1490643648"
               :title "Pu Wen Teji Shu Cha , 2013"
               :url   "https://moychay.com/catalog/puer/shu_puer_rassypnoj/puven-tetszi-shu-cha-2013-g"
               :price "$7.96"}
              {:image "https://moychay.com/system/product_fotos/images/000/400/550/small/_MG_2452.jpg?1490643340"
               :title "Bulanshan Shu Cha, 2013"
               :url   "https://moychay.com/catalog/puer/shu_puer_rassypnoj/bulanshan-shu-cha-2013-g"
               :price "$3.52"}
              {:image "https://moychay.com/system/product_fotos/images/000/400/554/small/_MG_2463.jpg?1490643411"
               :title "Menghai Shu Puer, 2013"
               :url   "https://moychay.com/catalog/puer/shu_puer_rassypnoj/menhay-shu-puer-2013-g"
               :price "$5.19"}
              {:image "https://moychay.com/system/product_fotos/images/000/400/525/small/MG_2374.jpg?1490036613"
               :title "Thermos # 22, 0,43 l."
               :url   "https://moychay.com/catalog/posuda/termosy/termos-22-430-ml"
               :price "$14.63"}
              {:image "https://moychay.com/system/product_fotos/images/000/400/358/small/_MG_2062.jpg?1489612114"
               :title "Handmade tea tray # 504, 44,5*26*7 cm"
               :url   "https://moychay.com/catalog/posuda/chabani_ruchnoy_raboty/chaban-ruchnoy-raboty-504-chaynaya-doska-445-26-7-sm"
               :price "$72.22"}
              {:image "https://moychay.com/system/product_fotos/images/000/400/414/small/_MG_2163.jpg?1489648853"
               :title "Handmade tea tray # 502, 48,5*21,5*7 cm"
               :url   "https://moychay.com/catalog/posuda/chabani_ruchnoy_raboty/chaban-ruchnoy-raboty-502-chaynaya-doska-485-215-7-sm"
               :price "$72.22"}
              {:image "https://moychay.com/system/product_fotos/images/000/400/420/small/_MG_2176.jpg?1489648953"
               :title "Handmade tea tray # 501, 49*22,5*8 cm"
               :url   "https://moychay.com/catalog/posuda/chabani_ruchnoy_raboty/chaban-ruchnoy-raboty-501-chaynaya-doska-49-225-8-sm"
               :price "$72.22"}
              {:image "https://moychay.com/system/product_fotos/images/000/400/423/small/_MG_2179.jpg?1489649035"
               :title "Handmade tea tray # 500, 40,5*26,5*7,5 cm"
               :url   "https://moychay.com/catalog/posuda/chabani_ruchnoy_raboty/chaban-ruchnoy-raboty-500-chaynaya-doska-405-265-75-sm"
               :price "$72.22"}
              {:image "https://moychay.com/system/product_fotos/images/000/400/457/small/_MG_2188.jpg?1489654594"
               :title "Handmade tea tray # 499, 39*25*6,5 cm"
               :url   "https://moychay.com/catalog/posuda/chabani_ruchnoy_raboty/chaban-ruchnoy-raboty-499-chaynaya-doska-39-25-65-sm"
               :price "$72.22"}
              {:image "https://moychay.com/system/product_fotos/images/000/400/426/small/_MG_2183.jpg?1489653715"
               :title "Handmade tea tray # 498, 38*22,5*7 cm"
               :url   "https://moychay.com/catalog/posuda/chabani_ruchnoy_raboty/chaban-ruchnoy-raboty-498-chaynaya-doska-38-225-7-sm"
               :price "$72.22"}
              {:image "https://moychay.com/system/product_fotos/images/000/400/469/small/_MG_2247.jpg?1489662472"
               :title "Handmade tea tray # 497,35,5*28,5*6 cm"
               :url   "https://moychay.com/catalog/posuda/chabani_ruchnoy_raboty/chaban-ruchnoy-raboty-497-chaynaya-doska-355-285-6-sm"
               :price "$72.22"}
              {:image "https://moychay.com/system/product_fotos/images/000/400/464/small/_MG_2239.jpg?1489662377"
               :title "Handmade tea tray # 495, 40*24,5*8 cm"
               :url   "https://moychay.com/catalog/posuda/chabani_ruchnoy_raboty/chaban-ruchnoy-raboty-495-chaynaya-doska-40-245-8-sm"
               :price "$72.22"}
              {:image "https://moychay.com/system/product_fotos/images/000/400/475/small/_MG_2257.jpg?1489662560"
               :title "Handmade tea tray # 492, 38,5*23,5*6 cm"
               :url   "https://moychay.com/catalog/posuda/chabani_ruchnoy_raboty/chaban-ruchnoy-raboty-492-chaynaya-doska-385-235-6-sm"
               :price "$72.22"}
              {:image "https://moychay.com/system/product_fotos/images/000/400/267/small/_MG_1711_%D0%BA%D0%BE%D0%BF%D0%B8%D1%8F.jpg?1489504445"
               :title "Handmade tea tray # 491, 40*27,5*7 cm"
               :url   "https://moychay.com/catalog/posuda/chabani_ruchnoy_raboty/chaban-ruchnoy-raboty-491-chaynaya-doska-40-275-7-sm"
               :price "$72.22"}
              {:image "https://moychay.com/system/product_fotos/images/000/400/394/small/_MG_2124.jpg?1489648284"
               :title "Handmade tea tray # 488, 37*24,5*7 cm"
               :url   "https://moychay.com/catalog/posuda/chabani_ruchnoy_raboty/chaban-ruchnoy-raboty-488-chaynaya-doska-37-245-7-sm"
               :price "$72.22"}
              {:image "https://moychay.com/system/product_fotos/images/000/400/439/small/_MG_2217.jpg?1489654212"
               :title "Handmade tea tray # 487, 36*25*7 cm"
               :url   "https://moychay.com/catalog/posuda/chabani_ruchnoy_raboty/chaban-ruchnoy-raboty-487-chaynaya-doska-36-25-7-sm"
               :price "$72.22"}
              {:image "https://moychay.com/system/product_fotos/images/000/400/442/small/_MG_2213.jpg?1489654311"
               :title "Handmade tea tray # 486, 38*26,5*6 cm"
               :url   "https://moychay.com/catalog/posuda/chabani_ruchnoy_raboty/chaban-ruchnoy-raboty-486-chaynaya-doska-38-265-6-sm"
               :price "$72.22"}
              {:image "https://moychay.com/system/product_fotos/images/000/400/391/small/_MG_2121.jpg?1489648213"
               :title "Handmade tea tray # 485, 37*22*7 cm"
               :url   "https://moychay.com/catalog/posuda/chabani_ruchnoy_raboty/chaban-ruchnoy-raboty-485-chaynaya-doska-37-22-7-sm"
               :price "$72.22"}] page)))))

