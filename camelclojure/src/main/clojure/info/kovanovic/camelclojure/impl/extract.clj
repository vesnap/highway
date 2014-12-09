(ns info.kovanovic.camelclojure.impl.extract
  (:use [info.kovanovic.camelclojure.impl.predicate :only (predicate)]
	[info.kovanovic.camelclojure.core :only (with-message)]))

(defn extract [r f]
  "Filtering component. TODO: write docstring"
  (.filter r (predicate f)))
