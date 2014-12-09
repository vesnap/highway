(ns info.kovanovic.camelclojure.impl.throttle
  (:use [info.kovanovic.camelclojure.util :only (apply-options)]))

(defn arg-map-throttle []
  {:period-millis #(.timePeriodMillis (first %) (second %))
   :async #(.asyncDelayed (first %))})

(defn throttle [r c opts]
  (apply-options (.throttle r c) opts (arg-map-throttle)))
