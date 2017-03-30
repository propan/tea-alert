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
      (is (= [{:image "http://yunnansourcing.com/img/p/4504-14135-home.jpg"
               :title "Yi Wu Mountain Wild Arbor Assamica Black Tea * Spring 2017"
               :url   "http://yunnansourcing.com/en/yunnan-black-teas/4504-yi-wu-mountain-wild-arbor-assamica-black-tea-spring-2017.html"
               :price "$8.50"}
              {:image "http://yunnansourcing.com/img/p/4503-14133-home.jpg"
               :title "Feng Qing Classic 58 Dian Hong Premium Yunnan Black tea * Spring 2017"
               :url   "http://yunnansourcing.com/en/yunnan-black-teas/4503-feng-qing-classic-58-dian-hong-premium-yunnan-black-tea-spring-2016.html"
               :price "$5.00"}
              {:image "http://yunnansourcing.com/img/p/4502-14126-home.jpg"
               :title "Traditional Process Dian Hong Black tea of Feng Qing * Spring 2017"
               :url   "http://yunnansourcing.com/en/yunnan-black-teas/4502-traditional-process-dian-hong-black-tea-of-feng-qing-spring-2017.html"
               :price "$5.00"}
              {:image "http://yunnansourcing.com/img/p/4501-14124-home.jpg"
               :title "Feng Qing Gold Tips Pure Bud Black Tea * Spring 2017"
               :url   "http://yunnansourcing.com/en/yunnan-black-teas/4501-feng-qing-gold-tips-pure-bud-black-tea-spring-2017.html"
               :price "$8.00"}
              {:image "http://yunnansourcing.com/img/p/4500-14119-home.jpg"
               :title "Imperial Gold Needle Yunnan Black tea * Spring 2017"
               :url   "http://yunnansourcing.com/en/yunnan-black-teas/4500-imperial-gold-needle-yunnan-black-tea-spring-2017.html"
               :price "$7.00"}
              {:image "http://yunnansourcing.com/img/p/4499-14113-home.jpg"
               :title "2016 Menghai 7592 1601 Ripe Pu-erh Tea Cake"
               :url   "http://yunnansourcing.com/en/menghaiteafactory/4499-2016-menghai-7592-1601-ripe-pu-erh-tea-cake.html"
               :price "$16.00"}
              {:image "http://yunnansourcing.com/img/p/4498-14111-home.jpg"
               :title "2006 Menghai Tea Factory \"7452\" Ripe Pu-erh Tea Cake"
               :url   "http://yunnansourcing.com/en/menghaiteafactory/4498-2006-menghai-tea-factory-7452-ripe-pu-erh-tea-cake.html"
               :price "$62.00"}
              {:image "http://yunnansourcing.com/img/p/4496-14102-home.jpg"
               :title "Jing Gu Sun-Dried Silver Needles White Pu-erh Tea Cake"
               :url   "http://yunnansourcing.com/en/whitetea/4496-jing-gu-sun-dried-silver-needles-white-pu-erh-tea-cake.html"
               :price "$9.00"}
              {:image "http://yunnansourcing.com/img/p/4495-14101-home.jpg"
               :title "Jinggu \"Da Jin Ya\" Camellia Taliensis Black Tea * Spring 2017"
               :url   "http://yunnansourcing.com/en/yunnan-black-teas/4495-jinggu-da-jin-ya-camellia-taliensis-black-tea-spring-2017.html"
               :price "$7.50"}
              {:image "http://yunnansourcing.com/img/p/4494-14096-home.jpg"
               :title "Yunnan Black Gold Black tea * Spring 2017"
               :url   "http://yunnansourcing.com/en/yunnan-black-teas/4494-yunnan-black-gold-black-tea-spring-2017.html"
               :price "$5.50"}
              {:image "http://yunnansourcing.com/img/p/4493-14092-home.jpg"
               :title "Ai Lao Mountain Jade Needle White Tea * Spring 2017"
               :url   "http://yunnansourcing.com/en/whitetea/4493-ai-lao-mountain-jade-needle-white-tea-spring-2017.html"
               :price "$7.00"}
              {:image "http://yunnansourcing.com/img/p/4492-14085-home.jpg"
               :title "Yunnan \"Pine Needles\" Green Tea from Mengku * Spring 2017"
               :url   "http://yunnansourcing.com/en/greentea/4492-yunnan-pine-needles-green-tea-from-mengku-spring-2017.html"
               :price "$6.50"}
              {:image "http://yunnansourcing.com/img/p/4491-14078-home.jpg"
               :title "Jingmai Mountain Wild Arbor Black tea of Spring 2017"
               :url   "http://yunnansourcing.com/en/yunnan-black-teas/4491-jingmai-mountain-wild-arbor-black-tea-of-spring-2017.html"
               :price "$4.00"}
              {:image "http://yunnansourcing.com/img/p/4490-14074-home.jpg"
               :title "Yi Mei Ren Wu Liang Mountain Yunnan Black Tea * Spring 2017"
               :url   "http://yunnansourcing.com/en/yunnan-black-teas/4490-yi-mei-ren-wu-liang-mountain-yunnan-black-tea-spring-2017.html"
               :price "$5.00"}
              {:image "http://yunnansourcing.com/img/p/4489-14068-home.jpg"
               :title "Yunnan \"Assamica Gold Needle\" Black Tea * Spring 2017"
               :url   "http://yunnansourcing.com/en/yunnan-black-teas/4489-yunnan-assamica-gold-needle-black-tea-spring-2017.html"
               :price "$58.00"}
              {:image "http://yunnansourcing.com/img/p/4488-14061-home.jpg"
               :title "Jing Gu White Pekoe Silver Needles White Tea * Spring 2017"
               :url   "http://yunnansourcing.com/en/whitetea/4488-jing-gu-white-pekoe-silver-needles-white-tea-spring-2017.html"
               :price "$6.50"}
              {:image "http://yunnansourcing.com/img/p/4487-14059-home.jpg"
               :title "Pure Silver 999 and Jing De Zhen Porcelain Cup * 35ml "
               :url   "http://yunnansourcing.com/en/silver-teapots-and-wares/4487-pure-silver-999-and-african-rosewood-cup-30ml-.html"
               :price "$31.00"}] page)))))
