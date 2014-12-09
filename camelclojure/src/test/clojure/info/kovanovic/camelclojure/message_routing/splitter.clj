(ns info.kovanovic.camelclojure.message-routing.splitter
  (:use [clojure.test]
        [info.kovanovic.camelclojure.dsl]
	[info.kovanovic.camelclojure.test-util]))

(deftest splitter-pattern
  (let [start (direct "start")
	end   (mock "end")
	camel (create (route (from start)
			     (split #(.split %1 ","))
			     (to end)))]
    (start-test camel start end)
    (send-text-message camel start "1,2")
    (is-message-count end 2)
    (stop-test camel)))
