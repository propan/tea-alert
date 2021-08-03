(ns tea-alert.parsers.baduchai-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [net.cgrand.enlive-html :as enlive]
            [tea-alert.parsers.baduchai :refer [parse]]))

(defn absolute-path
  [file]
  (str (.getPath (io/resource ".")) "../test-resources/" file))

(deftest parse-test
  (testing "Correctly parses source page"
    (let [page (-> (absolute-path "baduchai.html") (java.io.File.) (enlive/html-resource) (parse))]
      (is (= [{:image "https://baduchai.ru/wp-content/uploads/2021/04/ximg_0047-300x300.jpg.pagespeed.ic.Aqn4DB3Qf8.webp"
               :title "Ми Тао Хун  蜜桃红茶"
               :url   "https://baduchai.ru/shop/mi-tao-hun-%e8%9c%9c%e6%a1%83%e7%ba%a2%e8%8c%b6/"
               :price "$20.00"}
              {:image "https://baduchai.ru/wp-content/uploads/2020/08/x20200829_1552300-300x300.jpg.pagespeed.ic.QaUDTITeGZ.webp"
               :title "Мо Ли Сяо Чжун    茉莉小种"
               :url   "https://baduchai.ru/shop/mo-li-syao-chzhun-%e8%8c%89%e8%8e%89%e5%b0%8f%e7%a7%8d/"
               :price "$13.00"}
              {:image "https://baduchai.ru/wp-content/uploads/2020/06/x20200614_141058-300x300.jpg.pagespeed.ic.wRvDjngkAm.webp"
               :title "Лао Янь Сюнь Сяо Чжун  老烟熏小种"
               :url   "https://baduchai.ru/shop/lao-yan-syun-syao-chzhun-%e8%80%81%e7%83%9f%e7%86%8f%e5%b0%8f%e7%a7%8d/"
               :price "$30.00"}
              {:image "https://baduchai.ru/wp-content/uploads/2020/06/x1-300x300.jpg.pagespeed.ic.WNzCaFv39m.webp"
               :title "Мо Ли Цзюнь Мэй  茉莉骏眉"
               :url   "https://baduchai.ru/shop/mo-li-czzyun-mej-%e8%8c%89%e8%8e%89%e9%aa%8f%e7%9c%89/"
               :price "$26.00"}
              {:image "https://baduchai.ru/wp-content/uploads/2020/03/xwechat-image_20180927165427-300x300.jpg.pagespeed.ic.idMfVKoKrl.webp"
               :title "Некопчёный плитка  无烟铁盒"
               :url "https://baduchai.ru/shop/nekopchyonyj-plitka-%e6%97%a0%e7%83%9f%e9%93%81%e7%9b%92/"
               :price "$20.00"}
              {:image "https://baduchai.ru/wp-content/uploads/2019/11/xwechat-image_20180927165415-300x300.jpg.pagespeed.ic.thhw8986Pn.webp"
               :title "Копчёный плитка 烟熏铁盒"
               :url "https://baduchai.ru/shop/kopchyonyj-plitka-%e7%83%9f%e7%86%8f%e9%93%81%e7%9b%92/"
               :price "$20.00"}
              {:image "https://baduchai.ru/wp-content/uploads/2019/11/xp1017160-300x300.jpg.pagespeed.ic.oO069JENs5.webp"
               :title "Ту Хун 土红"
               :url   "https://baduchai.ru/shop/tu-xun/"
               :price "$12.00"}
              {:image "https://baduchai.ru/wp-content/uploads/2019/11/xp1017152-300x300.jpg.pagespeed.ic.Mur9vS8YBL.webp"
               :title "Ци Хун 祁红"
               :url   "https://baduchai.ru/shop/ci-xun/"
               :price "$22.00"}
              {:image "https://baduchai.ru/wp-content/uploads/2019/11/xp1016820-300x300.jpg.pagespeed.ic.JYXRFigX6V.webp"
               :title "Цзинь Цзюнь Мэй(金骏眉）"
               :url   "https://baduchai.ru/shop/czin-czyun-mej/"
               :price "$21.00"}
              {:image "https://baduchai.ru/wp-content/uploads/2019/11/xsuan-mej-tan-300x300.jpg.pagespeed.ic.xcm4AgF6AV.webp"
               :title "Суан Мэй Тан (酸梅汤)"
               :url   "https://baduchai.ru/shop/suan-mej-tan/"
               :price "$22.00"}
              {:image "https://baduchai.ru/wp-content/uploads/2019/11/xmej-chzhan-hun-300x300.jpg.pagespeed.ic.UVLSSknvfe.webp"
               :title "Мэй Чжань Хун (梅占红)"
               :url   "https://baduchai.ru/shop/mej-chzhan-xun/"
               :price "$11.00"}
              {:image "https://baduchai.ru/wp-content/uploads/2019/11/xtan-yan-gun-fu-300x300.jpg.pagespeed.ic.uuh9_LApY5.webp"
               :title "Тань Ян Гунфу (坦洋功夫)"
               :url   "https://baduchai.ru/shop/tan-yan-gunfu/"
               :price "$11.00"}
              {:image "https://baduchai.ru/wp-content/uploads/2019/11/xtun-mu-e-shen-chzhu-lin-syao-chzhun-300x300.jpg.pagespeed.ic.MpFFYt5cr8.webp"
               :title "Е Шен Чжу Линь Сяо Чжун (野生竹林小种)"
               :url   "https://baduchai.ru/shop/e-shen-chzhu-lin-syao-chzhun/"
               :price "$46.00"}
              {:image "https://baduchai.ru/wp-content/uploads/2019/11/xda-chi-gan1-300x300.jpg.pagespeed.ic.1lEbOovCpr.webp"
               :title "Да Чи Ган (大赤甘)"
               :url   "https://baduchai.ru/shop/da-chi-gan/"
               :price "$21.00"}
              {:image "https://baduchai.ru/wp-content/uploads/2019/11/xyan-syun-chzhen-shan-syao-chzhun-300x300.jpg.pagespeed.ic.8vgWOSFhwd.webp"
               :title "Янь СюньЧжен Шань Сяо Чжун копчёный (烟熏正山小种)"
               :url   "https://baduchai.ru/shop/chzhen-shan-syao-chzhun-trad-kopchyonyj/"
               :price "$21.00"}
              {:image "https://baduchai.ru/wp-content/uploads/2019/11/xgu-shu-dyan-hun-300x300.jpg.pagespeed.ic.oNl5fv-KoP.webp"
               :title "Гу Шу Дянь Хун (古树滇红)"
               :url   "https://baduchai.ru/shop/gu-shu-dyan-xun-dyan-xun-s-drevnix-derevev/"
               :price "$11.00"}
              {:image "https://baduchai.ru/wp-content/uploads/2019/11/xczzin-sy-dyan-hun-300x300.jpg.pagespeed.ic.3eDKWgyOZx.webp"
               :title "Цзинь Сы Дянь Хун (金丝滇红)"
               :url   "https://baduchai.ru/shop/czin-sy-dyan-xun-dyan-xun-zolotye-niti/"
               :price "$19.00"}] page)))))

