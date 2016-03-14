(ns prose.parsers.mutates
  (:require [om.next :as om]
            [prose.utils :as u]
            [prose.constants :as c]))

(defmulti mutate om/dispatch)

(defmethod mutate 'prose/inputChanged
  [{:keys [state]} _ params]
  {:action
   (fn []
     (swap! state update :prose/input #(:value params)))})

(defmethod mutate 'prose/createLine
  [{:keys [state]} key params]
  (let [st @state
        line (u/make-line (get st :prose/input))
        ref (u/make-line-ref (:id line))]
    {:action
     (fn []
       (swap! state assoc :prose/input "")
       (swap! state update :lines #(conj % ref))
       (swap! state assoc-in [c/line-ref-name (:id line)] line))}))
