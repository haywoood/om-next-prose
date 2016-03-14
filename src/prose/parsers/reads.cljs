(ns prose.parsers.reads
  (:require [om.next :as om]))

(defmulti read om/dispatch)

(defmethod read :default
  [{:keys [state query] :as env} key params]
  (let [st @state]
    {:value (om/db->tree query (get st key) st)}))

(defmethod read :prose/input
  [{:keys [state]}]
  {:value (get @state :prose/input)})