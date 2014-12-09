(ns info.kovanovic.camelclojure.impl.from
  (:use [info.kovanovic.camelclojure.util :only (apply-options)]))

(defn- arg-map []
     {:event         #(.inOnly (first %))
      :request-reply #(.inOut  (first %))})

(defn from [r url opts]
  "TODO: write docstring"
  (apply-options (.from r url) opts (arg-map)))
