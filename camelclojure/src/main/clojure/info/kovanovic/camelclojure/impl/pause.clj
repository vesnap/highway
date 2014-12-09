(ns info.kovanovic.camelclojure.impl.pause)

(defn pause [r milis]
  "TODO: write docstring"
  (.delay r (long milis)))
