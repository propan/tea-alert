(ns tea-alert.templates.elements
  (:require [hiccup.def :refer [defelem]]))

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
