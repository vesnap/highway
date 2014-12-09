(ns info.kovanovic.camelclojure.impl.aggregator
  (:use [info.kovanovic.camelclojure.util :only (apply-options)]
	[info.kovanovic.camelclojure.impl.predicate :only (predicate)])
  (:import (org.apache.camel.processor.aggregate AggregationStrategy)))

(defn generate-aggregator [agg-fn]
  (fn [ex1 ex2]
    (if-not (nil? ex1)
      (let [body1    (.. ex1 getIn getBody)
	    headers1 (.. ex1 getIn getHeaders)
	    body2    (.. ex2 getIn getBody)
	    headers2 (.. ex2 getIn getHeaders)]
	(agg-fn [body1, headers1]
		[body2, headers2]))
      (let [body2    (.. ex2 getIn getBody)
	    headers2 (.. ex2 getIn getHeaders)]
	(agg-fn [nil, nil]
		[body2, headers2])))))

(defn- aggregation-strategy [agg-fn]
  (proxy [AggregationStrategy] []
    (aggregate [old-exchange new-exchange]
	       (let [[n-body n-headers] ((generate-aggregator agg-fn) old-exchange new-exchange)]
		 (.. new-exchange getIn (setBody n-body))
		 (.. new-exchange getIn (setHeaders n-headers))
		 new-exchange))))

(defn- arg-map-as []
  {:timeout   #(.completionTimeout  (first %) (second %))
   :count     #(.completionSize      (first %) (second %))
   :predicate #(.completionPredicate (first %) (predicate (second %)))
   :interval  #(.completionInterval  (first %) (second %))})

(defn aggregator[r fn h opts]
  "aggregating component. TODO: write docstring"
  (apply-options (.. r (aggregate (aggregation-strategy fn)) (header h)) opts (arg-map-as)))


