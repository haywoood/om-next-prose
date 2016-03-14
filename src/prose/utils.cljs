(ns prose.utils
  (:require [om.next :as om]
            [prose.constants :as c]))

(defn make-line [text]
  {:id (om/tempid)
   :text text})

(defn make-line-ref [id]
  [c/line-ref-name id])
