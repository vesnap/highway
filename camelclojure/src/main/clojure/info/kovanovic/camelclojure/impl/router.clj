(ns info.kovanovic.camelclojure.impl.router
  (:use [info.kovanovic.camelclojure.impl.predicate :only (predicate)]
	[info.kovanovic.camelclojure.core :only (with-message)]))

(defn- add-branch [r f dest]
  (.. r (when (predicate f)) (to dest)))

(defn- add-branches [r f-dest-pairs]
  (if-not (empty? f-dest-pairs)
    (let [[f dest] (first f-dest-pairs)]
      (recur (add-branch r f dest)
	     (rest f-dest-pairs)))
    r))

(defn router [r args]
  (let [r2 (add-branches (.choice r) (partition 2 2 args))]
    (if (odd? (count args))
      (.. r2 otherwise (to (last args)) end)
      (.end r2))))
