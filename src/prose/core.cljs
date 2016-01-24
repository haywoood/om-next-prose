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

(defui Line-Input
  om/IQuery
  (query [this]
    {:line-in-progress [:text]})

  Object
  (render [this]
    (let [{:keys [text]} (om/props this)]
      (dom/div nil text))))

(def line-input (om/factory Line-Input))

(defui Stanza
  om/Ident
  (ident [this {:keys [id]}]
    [:prose/stanzas-by-id id])

  om/IQuery
  (query [this]
    `[:id :title {:prose/lines ~(om/get-query Line)}])

  Object
  (render [this]
    (let [{:keys [id title prose/lines]} (om/props this)]
      (dom/li nil
              (str id " " title)
              (apply dom/div nil (map line lines))))))

(def stanza (om/factory Stanza {:key-fn :id}))

(defui Prose
  om/IQuery
  (query [this]
    `[~(om/get-query Line-Input)
      {:prose/stanzas ~(om/get-query Stanza)}])

  Object
  (render [this]
    (let [{:keys [prose/stanzas line-in-progress]} (om/props this)]
      (dom/div nil
               (apply dom/ul nil (map stanza stanzas))
               (line-input line-in-progress)))))

(om/add-root! reconciler/reconciler Prose (gdom/getElement "app"))