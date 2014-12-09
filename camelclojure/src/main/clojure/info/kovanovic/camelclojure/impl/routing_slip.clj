(ns info.kovanovic.camelclojure.impl.routing-slip
  (:use [info.kovanovic.camelclojure.util :only (apply-options)]))

(defn arg-map-rs []
  {:ignore-invalid #(.ignoreInvalidEndpoints (first %))
   :delimeter #(.uriDelimiter (first %) (second %))})

(defn routing-slip [r h opts]
  (apply-options (.. r routingSlip (header h)) opts (arg-map-rs)))
