(ns prose.reconciler
  (:require [om.next :as om]
            [prose.state :as state]))

(defmulti read om/dispatch)

(defmethod read :default
  [{:keys [query state]} k _]
  (let [st @state]
    (if-let [value (om/db->tree query (get st k) st)]
      {:value value}
      {:value :not-found})))

(def reconciler
  (om/reconciler {:state  state/state
                  :parser (om/parser {:read   read
                                      :mutate (fn [e])})}))