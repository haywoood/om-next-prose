(ns prose.core
  (:require [goog.dom :as gdom]
            [om.next :as om]
            [prose.reconciler :refer [reconciler]]
            [prose.components.app :refer [ProseApp]]))

(enable-console-print!)

(om/add-root! reconciler ProseApp (gdom/getElement "app"))