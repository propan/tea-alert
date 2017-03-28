(ns tea-alert.parsers.essenceoftea-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [net.cgrand.enlive-html :as enlive]
            [tea-alert.parsers.essenceoftea :refer [parse]]))

(defn absolute-path
  [file]
  (str (.getPath (io/resource ".")) "../test-resources/" file))

(deftest parse-test
  (testing "Correctly parses source page"
    (let [page (-> (absolute-path "essenceoftea.html") (java.io.File.) (enlive/html-resource) (parse))]
      (is (= [{:image "https://www.essenceoftea.com/media/catalog/product/cache/1/small_image/300x/040ec09b1e35df139433887a97daa66f/d/s/dscf2187.jpg"
               :title "110ml 1980's Ba Gua Zini Factory 1"
               :url   "https://www.essenceoftea.com/110ml-1980-s-ba-gua-zini-factory-1.html"
               :price "£138.00"}
              {:image "https://www.essenceoftea.com/media/catalog/product/cache/1/small_image/300x/040ec09b1e35df139433887a97daa66f/d/s/dscf2175_2.jpg"
               :title "120ml 1980's Factory 1 Heisha "
               :url   "https://www.essenceoftea.com/120ml-1980-s-factory-1-heisha.html"
               :price "£148.00"}
              {:image "https://www.essenceoftea.com/media/catalog/product/cache/1/small_image/300x/040ec09b1e35df139433887a97daa66f/d/s/dscf2104-3.jpg"
               :title "140ml Private order zini xishi"
               :url   "https://www.essenceoftea.com/140ml-private-order-zini-xishi.html"
               :price "£166.00"}
              {:image "https://www.essenceoftea.com/media/catalog/product/cache/1/small_image/300x/040ec09b1e35df139433887a97daa66f/d/s/dscf2165_1.jpg"
               :title "110ml Private order A"
               :url   "https://www.essenceoftea.com/110ml-private-order-a.html"
               :price "£166.00"}
              {:image "https://www.essenceoftea.com/media/catalog/product/cache/1/small_image/300x/040ec09b1e35df139433887a97daa66f/d/s/dscf2171.jpg"
               :title "110ml Private order B"
               :url   "https://www.essenceoftea.com/110ml-private-order-b.html"
               :price "£166.00"}
              {:image "https://www.essenceoftea.com/media/catalog/product/cache/1/small_image/300x/040ec09b1e35df139433887a97daa66f/d/s/dscf2180.jpg"
               :title "200ml 1980's Yu Hua Long Factory one"
               :url   "https://www.essenceoftea.com/200ml-1980-s-yu-hua-long-factory-one.html"
               :price "£138.00"}
              {:image "https://www.essenceoftea.com/media/catalog/product/cache/1/small_image/300x/040ec09b1e35df139433887a97daa66f/1/-/1-100.jpg"
               :title "50ml 1990's Factory 5 Half Moon"
               :url   "https://www.essenceoftea.com/50ml-1990-s-factory-5-half-moon.html"
               :price "£50.00"}] page)))))
