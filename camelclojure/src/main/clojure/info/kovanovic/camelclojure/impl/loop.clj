(ns info.kovanovic.camelclojure.impl.loop
  (:use [info.kovanovic.camelclojure.util :only (apply-options)]))

(defn arg-map-loop []
  {:times  #(.constant (first %) (second %))
   :header #(.header   (first %) (second %))})

(defn doloop [r opts]
  (apply-options (.loop r) opts (arg-map-loop)))
