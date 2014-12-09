(ns info.kovanovic.camelclojure.message-construction.request-reply
  (:use [clojure.test]
        [info.kovanovic.camelclojure.dsl]
	[info.kovanovic.camelclojure.test-util]))

(deftest request-reply-pattern
  (let [start (direct "start")
	end   (mock "end")

	camel (create (route (from start :request-reply)
			     (to end)))]
    
    (start-test camel start end)
    (send-text-message camel start "m1")
    (send-text-message camel start "m2")
    (is-message-count end 2)
    (stop-test camel)))
