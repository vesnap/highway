(ns info.kovanovic.camelclojure.impl.process
  (:import (org.apache.camel Processor)))


(defn processor [f]
  (proxy [Processor] []
    (process [ex]
	     (let [body (.. ex getIn getBody)
		   headers (.. ex getIn getHeaders)
		   msg (list body headers)
		   n-msg (f msg)]
	       (.. ex getIn (setBody (first n-msg)))
	       (.. ex getIn (setHeaders (second n-msg))))
	     nil)))

(defn process [r f]
  "TODO: write docstring"
  (.. r (process (processor f))))

