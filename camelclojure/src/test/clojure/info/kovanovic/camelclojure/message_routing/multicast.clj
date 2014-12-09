(ns info.kovanovic.camelclojure.message-routing.multicast
  (:use [clojure.test]
        [info.kovanovic.camelclojure.dsl]
	[info.kovanovic.camelclojure.test-util]))

(deftest multicast-pattern
  (let [start (direct "start")
	end1  (mock "end1")
	end2  (mock "end2")
	end3  (mock "end3")
	
	camel (create (route (from start)
			     (to :multicast
				 end1
				 end2
				 end3)))]
    (start-test camel start end1 end2 end3)
    (send-text-message camel start "m1")
    (send-text-message camel start "m2")
    (send-text-message camel start "m3")
    (is-message-count end1 3)
    (is-message-count end2 3)
    (is-message-count end3 3)
    (stop-test camel)))

