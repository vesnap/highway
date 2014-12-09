(ns info.kovanovic.camelclojure.impl.recipient-list
  (:use [info.kovanovic.camelclojure.util :only (apply-options)])
  (:import (org.apache.camel.builder Builder)))


(defn arg-map-rl []
  {:paralel #(.parallelProcessing (first %))
   :timeout #(.timeout (first %) (second %))
   :stop-on-exception #(.stopOnException (first %))
   :ignore-invalid-endpoints #(.ignoreInvalidEndpoints (first %))})

(defn recipient-list [r h opts]
  (apply-options (.. r (recipientList (Builder/header h))) opts (arg-map-rl)))
