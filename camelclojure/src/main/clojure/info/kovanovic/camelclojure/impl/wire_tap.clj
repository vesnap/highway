(ns info.kovanovic.camelclojure.impl.wire-tap)

(defn wire-tap [r url]
  (.wireTap r url))
