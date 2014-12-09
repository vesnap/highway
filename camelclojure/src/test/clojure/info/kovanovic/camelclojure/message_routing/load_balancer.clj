(ns info.kovanovic.camelclojure.message-routing.load-balancer
  (:use [clojure.test]
        [info.kovanovic.camelclojure.dsl]
	[info.kovanovic.camelclojure.test-util]))
  
(deftest load-balancer-pattern-round-robin
  (let [start (direct "start")
	end1  (mock "end1")
	end2  (mock "end2")
	end3  (mock "end3")
	
	camel (create (route (from start)
			     (load-balancer :round-robin :to end1 end2 end3)))]
    (start-test camel start end1 end2 end3)
    (dotimes [_ 30]
      (send-text-message camel start "message"))
    (is-message-count end1 10)
    (is-message-count end2 10)
    (is-message-count end3 10)
    (stop-test camel)))
