(ns info.kovanovic.camelclojure.message-construction.event-message
  (:use [clojure.test]
        [midje.sweet]
        [info.kovanovic.camelclojure.dsl]
	[info.kovanovic.camelclojure.test-util]))

(deftest event-message-pattern
  (let [start (direct "start")
	end   (mock "end")
	camel (create (route (from start :event)
			     (to end)))]
    (start-test camel start end)
    (send-text-message camel start "m1")
    (send-text-message camel start "m2")
    (is-message-count end 2)
    (stop-test camel)))
 
