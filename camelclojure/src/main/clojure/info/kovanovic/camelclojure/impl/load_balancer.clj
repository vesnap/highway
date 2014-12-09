(ns info.kovanovic.camelclojure.impl.load-balancer
  (:use [info.kovanovic.camelclojure.util :only (parse-options)]
	[info.kovanovic.camelclojure.impl.to :only (to)]))

(defn load-balancer [r t options]
  (let [opts (parse-options options)
	applied-lb (let [lb (.loadBalance r)
			 weights (:weighted opts)]
		     (if weights
		       (.weighted lb (:round-robbin t) weights)
		       (cond
			(= t :round-robin) (.roundRobin lb)
			(= t :random) (.random lb)
			true (throw (IllegalArgumentException.
				     "invalid type: :round-robin and :random allowed")))))]
    (to applied-lb (:to opts))))
