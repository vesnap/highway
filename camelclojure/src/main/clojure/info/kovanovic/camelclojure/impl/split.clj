(ns info.kovanovic.camelclojure.impl.split
  (:use [info.kovanovic.camelclojure.util :only (arg-count)])
  (:import (info.kovanovic.camelclojure AbstractSplitter)))

(defn- splitter [f]
  (proxy [AbstractSplitter] []
    (split [body message exchange]
	   (condp = (arg-count f)
	       1 (seq (f body))
	       2 (seq (f body message))
	       3 (seq (f body message exchange))))))

(defn split [r f]
  "TODO: write docstring"
  (.. r split (method (splitter f) "split")))
