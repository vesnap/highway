(ns info.kovanovic.camelclojure.util
  (:import (java.util.concurrent TimeUnit)))

;;; Utilities

(defn parse-options
  "create hashmap out of list of keywords and symbols.
from (:a 1 :b 2 3 :c 3 4 5) create {:a (1) :b (2 3) :c (3 4 5)"
  ([args]
     (parse-options (rest args) () (first args) ()))
  
  ([args-left parsed current-arg current-arg-value]
     (if-let [current (first args-left)]        
       (if (keyword? current)
	 (recur (rest args-left)
		(cons current-arg (cons current-arg-value parsed))
		current
		())
	 (recur (rest args-left)
		parsed
		current-arg
		(cons current current-arg-value)))
       (apply sorted-map (cons current-arg (cons current-arg-value parsed))))))

(defn- create-options-applier [keyword-option-map]
  (fn [route keyword-args-map]
    (let [option-keyword (first keyword-args-map)
	  ;;add route object in the argument list as first element
	  option-args (cons route (second keyword-args-map)) 
	  option-function (get keyword-option-map option-keyword)]
      (if-not (nil? option-function)
	(option-function option-args)
	route))))

(defn apply-options [route options options-map]
  (let [options-applier (create-options-applier options-map)]
    (reduce options-applier route (parse-options options))))

(defn third [col]
  (second (rest col)))

(defn fourth [col]
  (second (rest (rest col))))


(defn arg-count [f]
  "count the number of arguments of function"
  (let [m (first (.getDeclaredMethods (class f)))
	p (.getParameterTypes m)]
    (alength p)))

(def unit-enums
     #{:microseconds TimeUnit/MICROSECONDS
       :milliseconds TimeUnit/MILLISECONDS
       :nanoseconds TimeUnit/NANOSECONDS
       :seconds TimeUnit/SECONDS})

(def unit-converters
     #{:minutes (partial * 60)
       :hours (partial * 60 60)
       :days (partial * 60 60 24)})

(defn convert-unit-to-seconds [amount timeunit]
  (if-let [convert-fn (get unit-converters timeunit)]
    (convert-fn amount)))
