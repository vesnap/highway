(ns info.kovanovic.camelclojure.impl.predicate
  (:use [info.kovanovic.camelclojure.core :only (with-message)])
  (:import (org.apache.camel Predicate)))

(defn predicate [f]
  (proxy [Predicate] []
    (matches [exchange]
	     ((with-message f) exchange))))

