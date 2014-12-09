(ns info.kovanovic.camelclojure.message-routing.loop
  (:use [clojure.test]
        [info.kovanovic.camelclojure.dsl]
	[info.kovanovic.camelclojure.test-util]))
  
(deftest loop-pattern
  (let [start (direct "start")
	end  (mock "end")

	camel (create (route (from start)
			     (doloop :times 10)
			     (to end)))]
    (start-test camel start end)
    (send-text-message camel start "message")
    (is-message-count end 10)
    (stop-test camel)))
