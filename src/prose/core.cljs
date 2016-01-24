(ns prose.core
  (:require [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [prose.reconciler :as reconciler]))

(enable-console-print!)

(defui Line
  om/Ident
  (ident [this {:keys [id]}]
    [:prose/lines-by-id id])

  om/IQuery
  (query [this]
    '[:id :text])

  Object
  (render [this]
    (let [{:keys [id text]} (om/props this)]
      (dom/div nil (str id " " text)))))

(def line (om/factory Line {:key-fn :id}))

(defui Stanza
  om/Ident
  (ident [this {:keys [id]}]
    [:prose/stanzas-by-id id])

  om/IQuery
  (query [this]
    [:id :title `{:prose/lines ~(om/get-query Line)}])

  Object
  (render [this]
    (let [{:keys [id title] :as props} (om/props this)
          lines (:prose/lines props)]
      (dom/li nil
              (str id " " title)
              (apply dom/div nil (map line lines))))))

(def stanza (om/factory Stanza {:key-fn :id}))

(defui Prose
  om/IQuery
  (query [this]
    `[{:prose/stanzas ~(om/get-query Stanza)}])

  Object
  (render [this]
    (let [props (om/props this)
          stanzas (:prose/stanzas props)]
      (apply dom/ul nil (map stanza stanzas)))))

(om/add-root! reconciler/reconciler Prose (gdom/getElement "app"))