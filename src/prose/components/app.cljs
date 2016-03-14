(ns prose.components.app
  (:require [om.dom :as dom]
            [om.next :as om :refer-macros [defui]]
            [cljs.pprint :as pp]
            [prose.utils :as u]))

(defui Line
       om/Ident
       (ident [this {:keys [id]}]
              (u/make-line-ref id))
       om/IQuery
       (query [this]
              [:id :text])
       Object
       (render [this]
               (let [{:keys [id text] :as props} (om/props this)]
                 (dom/h3 nil text))))

(def line (om/factory Line {:key-fn :id}))

(defui Stanza
       om/Ident
       (ident [this {:keys [id]}]
              [:stanzas/by-id id])
       om/IQueryParams
       (params [this]
               {:lines (om/get-query Line)})
       om/IQuery
       (query [this]
              '[:id :title {:lines ?lines}])
       Object
       (render [this]
               (let [{:keys [title lines] :as props} (om/props this)]
                 (dom/div nil
                          (dom/h1 nil title)
                          (apply dom/div nil (map line lines))))))

(def stanza (om/factory Stanza {:key-fn :id}))

(defn handleInputChange [component]
  (fn [e]
    (let [form-val (-> e .-target .-value)]
      (om/transact! component `[(prose/inputChanged {:value ~form-val}) :prose/input]))))

(defn proseForm [component {:keys [value]}]
  (dom/form #js {:onSubmit #(do
                             (.preventDefault %)
                             (om/transact! component '[(prose/createLine) :prose/input :lines]))}
            (dom/input #js {:type     "text"
                            :value    value
                            :onChange (handleInputChange component)})))

(defui ProseApp
       om/IQueryParams
       (params [this]
               {:stanza (om/get-query Stanza)
                :line (om/get-query Line)})
       om/IQuery
       (query [this]
              '[:prose/input {:stanzas ?stanza} {:lines ?line}])
       Object
       (render [this]
               (let [{:keys [stanzas prose/input lines] :as props} (om/props this)]
                 (println (pp/pprint (om/props this)))
                 (dom/div nil
                          (proseForm this {:value input})
                          (apply dom/div nil
                                 (map line lines))))))