(ns tea-alert.templates.error-alert
  (:require [tea-alert.templates.elements :refer [table]]
            [hiccup.page :refer [html5]]
            [hiccup.element :refer [link-to]]))

(defn render
  [ex]
  (html5
   [:head
    [:title "Tea Alert - Error"]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]]
   [:body {:width "100%" :bgcolor "#ffffff" :style "margin: 0;"}
    [:center {:style "width: 100%; background: #ffffff;"}
     [:div {:style "max-width: 680px; margin: auto;"}
      (table "max-width: 680px;"
             [:tbody
              [:tr
               [:td {:style "padding: 20px 10px;"}]]
              [:tr
               [:td {:bgcolor "#ffffff" :style "font-family: sans-serif; font-size: 14px; padding: 20px 15px 0 13px; color: #464646; line-height: 20px;"}
                [:p {:style "padding: 0 0 10px 0; margin: 0; color: #333;"}
                 "Houston, we've had a problem."]
                [:p {:style "padding: 0 0 5px 0; margin: 0;"}
                 "We've had a main B bus undervolt. I hope you know what it means:"]]]
              [:tr
               [:td {:bgcolor "#ffffff" :style "padding: 0 5px;"}
                [:pre {:style "width: 680px; overflow: scroll; border: 1px solid #c0c0c0; padding: 5px 10px; line-height: 20px; background-color: #f0f0f0; font-size: 12px;"}
                 (->> ex (.getStackTrace) (map str) (clojure.string/join "\n"))]]]
              [:tr
               [:td {:bgcolor "#ffffff" :style "font-family: sans-serif; font-size: 14px; padding: 20px 13px 10px 13px; color: #464646; line-height: 20px;"}
                [:p {:style "padding: 0 0 5px 0; margin: 0;"}
                 "Always searching for you,"]
                [:p {:style "padding: 0 0 5px 0; margin: 0; color: #444;"}
                 "Tea Alert."]]]
              [:tr
               [:td {:style "padding: 20px 10px;"}]]])]]]))

