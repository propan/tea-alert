(ns tea-alert.parsers.badurov-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [net.cgrand.enlive-html :as enlive]
            [tea-alert.parsers.badurov :refer [parse]]))

(defn absolute-path
  [file]
  (str (.getPath (io/resource ".")) "../test-resources/" file))

(deftest parse-test
  (testing "Correctly parses source page"
    (let [page (-> (absolute-path "badurov.html") (java.io.File.) (enlive/html-resource) (parse))]
      (is (= [{:image "http://badurov.ru/wp-content/uploads/bfi_thumb/WeChat-Image_20180927165427-nwqardvcihr4hjeri7rmwhltkugmcvjtuv34nimebk.jpg"
               :title "Некопчёный плитка 大赤甘铁盒"
               :url   "http://badurov.ru/shop/tea/nekopchyonyj-plitka-%e5%a4%a7%e8%b5%a4%e7%94%98%e9%93%81%e7%9b%92/"
               :price "$20.00"}
              {:image "http://badurov.ru/wp-content/uploads/bfi_thumb/WeChat-Image_20180927165415-nwqavzaxxc1l8wqgo79z3buy4nt7zlsl5lwl47t3wg.jpg",
               :title "Копчёный плитка 烟熏铁盒"
               :url   "http://badurov.ru/shop/tea/kopchyonyj-plitka-%e7%83%9f%e7%86%8f%e9%93%81%e7%9b%92/"
               :price "$20.00"}
              {:image "http://badurov.ru/wp-content/uploads/bfi_thumb/P1017160-n8fzm05awpwpxe7336ibbony5enpohqbnxrm359kao.jpg"
               :title "Ту Хун 土红"
               :url   "http://badurov.ru/shop/tea/tu-xun/"
               :price "$12.00"}
              {:image "http://badurov.ru/wp-content/uploads/bfi_thumb/P1017152-n8fz26pstgs35czdt44h96p39yc4e821xum2u4ndhc.jpg"
               :title "Ци Хун 祁红"
               :url   "http://badurov.ru/shop/tea/ci-xun/"
               :price "$22.00"}
              {:image "http://badurov.ru/wp-content/uploads/bfi_thumb/P1016820-n73x6igr622subvg041668h0k37e6ly6hvtxslet4w.jpg"
               :title "Цзинь Цзюнь Мэй(金骏眉）"
               :url   "http://badurov.ru/shop/tea/krasnyj/czin-czyun-mej/"
               :price "$21.00"}
              {:image "http://badurov.ru/wp-content/uploads/bfi_thumb/Суан-Мэй-Тан-mr9ou832rb0kz82pfiyf0fihtc528cmqn2hiui5qm8.jpg"
               :title "Суан Мэй Тан (酸梅汤)"
               :url   "http://badurov.ru/shop/tea/suan-mej-tan/"
               :price "$22.00"}
              {:image "http://badurov.ru/wp-content/uploads/bfi_thumb/Мэй-Чжань-Хун-mr9opjtyrym990v3q08743yzhd6cyj2sbxplxz37k0.jpg"
               :title "Мэй Чжань Хун (梅占红)"
               :url   "http://badurov.ru/shop/tea/mej-chzhan-xun/"
               :price "$11.00"}
              {:image "http://badurov.ru/wp-content/uploads/bfi_thumb/Тань-Ян-Гун-Фу-ma5n4pm8wv0ddz0b5b4jwrfzr1uweouiua0q0c3z0g.jpg"
               :title "Тань Ян Гунфу (坦洋功夫)"
               :url   "http://badurov.ru/shop/tea/tan-yan-gunfu/"
               :price "$11.00"}
              {:image "http://badurov.ru/wp-content/uploads/bfi_thumb/Тун-Му-Е-Шен-Чжу-Линь-Сяо-Чжун-ma5n9xlyvq5pvzf8nkdxrg06k44b65ku84htzod8g0.jpg"
               :title "Е Шен Чжу Линь Сяо Чжун (野生竹林小种)"
               :url   "http://badurov.ru/shop/tea/e-shen-chzhu-lin-syao-chzhun/"
               :price "$46.00"}
              {:image "http://badurov.ru/wp-content/uploads/bfi_thumb/Да-Чи-Ган1-ma5kgba40j1qpn161n1y7366kgzr065ix5g26r1h4w.jpg"
               :title "Да Чи Ган (大赤甘)"
               :url   "http://badurov.ru/shop/tea/da-chi-gan/"
               :price "$21.00"}] page)))))
