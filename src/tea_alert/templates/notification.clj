(ns tea-alert.templates.notification
  (:require [hiccup.def :refer [defelem]]
            [hiccup.page :refer [html5]]
            [hiccup.element :refer [link-to]]))

(defelem table
  [style & body]
  (->
   [:table {:role        "presentation"
            :cellspacing "0"
            :cellpadding "0"
            :border      "0"
            :align       "center"
            :width       "100%"
            :style       style}]
   (concat body)
   (vec)))

(defn render
  [recipient items]
  (html5
   [:head
    [:title "Tea Alert"]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]]
   [:body {:width "100%" :bgcolor "#303030" :style "margin: 0;"}
    [:center {:style "width: 100%; background: #303030;"}
     [:div {:style "max-width: 680px; margin: auto;"}
      (table "max-width: 680px;"
             [:tbody
              [:tr
               [:td {:style "padding: 20px 10px;"}]]
              [:tr
               [:td {:bgcolor "#ffffff" :style "font-family: sans-serif; font-size: 14px; padding: 20px 15px 10px 13px; color: #464646; line-height: 20px;"}
                [:p {:style "padding: 0 0 10px 0; margin: 0; color: #333;"}
                 (str "Hello " (:name recipient) ",")]
                [:p {:style "padding: 0 0 5px 0; margin: 0;"}
                 "I've got some news for you. I just found a few new tea related items you might be interested in!"]]]
              [:tr
               [:td {:bgcolor "#ffffff" :style "padding: 0 10px;"}
                (table "border:1px solid #eaeaea;"
                       [:thead
                        [:tr
                         [:th {:align   "left"
                               :bgcolor "#EAEAEA"
                               :style   "font-family: sans-serif; font-size: 14px; padding:5px 9px"}
                          "Item"]
                         [:th {:align   "center"
                               :bgcolor "#EAEAEA"
                               :width   "100px"
                               :style   "font-family: sans-serif; font-size: 14px; padding:5px 9px"}
                          "Price"]]]
                       [:tbody
                        (for [[index {:keys [price title url]}] (map-indexed vector items)]
                          (let [border (if (= index (- (count items) 1)) "" "border-bottom:1px dotted #cccccc;")]
                            [:tr
                             [:td {:align  "left"
                                   :valign "top"
                                   :style  (str "font-family: sans-serif; font-size: 13px; padding:5px 9px;" border)}
                              (link-to {:style "color: #F07860; text-decoration: none;"} url title)]
                             [:td {:align  "center"
                                   :valign "top"
                                   :style  (str "font-family: sans-serif; font-size: 13px; padding:5px 9px;" border)}
                              (or price "-")]]))])]]
              [:tr
               [:td {:bgcolor "#ffffff" :style "font-family: sans-serif; font-size: 14px; padding: 20px 13px 10px 13px; color: #464646; line-height: 20px;"}
                [:p {:style "padding: 0 0 5px 0; margin: 0;"}
                 "Always searching for you,"]
                [:p {:style "padding: 0 0 5px 0; margin: 0; color: #444;"}
                 "Tea Alert."]]]
              [:tr
               [:td {:style "padding: 20px 10px;"}]]])]]]))
