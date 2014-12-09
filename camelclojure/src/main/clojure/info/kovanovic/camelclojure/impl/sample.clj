(ns info.kovanovic.camelclojure.impl.sample
  (:use [info.kovanovic.camelclojure.util :only (apply-options
						 unit-enums
						 unit-converters
						 convert-unit-to-seconds)]))


(defn arg-map-sample []
  {:period #(let [amount (second %)
		  unit (second (rest %))]
	      (if-let [sec-val (convert-unit-to-seconds amount unit)]
		(.. % (samplePeriod sec-val) (unit-enums :seconds))
		(.. % (samplePeriod amount) (unit-enums unit))))
   :every #(.sampleMessageFrequency (first %) (second %))})

(defn sample [r opts]
  (apply-options (.sample r) opts (arg-map-sample)))
