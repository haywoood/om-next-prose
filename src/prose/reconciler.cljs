(ns prose.reconciler
  (:require [om.next :as om]
            [prose.parsers.reads :refer [read]]
            [prose.parsers.mutates :refer [mutate]]))

(def state {:prose/input ""
            :lines [{:id 1 :text "burger king"}
                    {:id 2 :text "sven diagram"}
                    {:id 3 :text "the laughing zebra"}
                    {:id 4 :text "baskets"}]
            :stanzas [{:id 1
                       :title "slap chopp"
                       :lines [{:id 1 :text "burger king"}
                               {:id 2 :text "sven diagram"}
                               {:id 3 :text "the laughing zebra"}]}
                      {:id 2
                       :title "elephants"
                       :lines [{:id 1 :text "burger king"}
                               {:id 2 :text "sven diagram"}
                               {:id 4 :text "baskets"}]}]})

(def parser (om/parser {:read read :mutate mutate}))

(def reconciler (om/reconciler {:state state :parser parser}))