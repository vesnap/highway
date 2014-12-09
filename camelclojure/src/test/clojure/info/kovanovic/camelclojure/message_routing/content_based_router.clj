(ns info.kovanovic.camelclojure.message-routing.content-based-router
  (:use [clojure.test]
        [info.kovanovic.camelclojure.dsl]
	[info.kovanovic.camelclojure.test-util]))

(deftest content-based-router-pattern
  (let [start (direct "start")
	end1  (mock "end1")
	end2  (mock "end2")
	end-def (mock "end-def")
	body-equals #(fn [[body _]]
			(= body %))
	
	camel (create (route (from start)
			     (router (body-equals "end1")
				     end1
				     (body-equals "end2")
				     end2
				     end-def)))]
    
    (start-test camel start end1 end2 end-def)
    (send-text-message camel start "end1")
    (send-text-message camel start "end2")
    (send-text-message camel start "m1")
    (send-text-message camel start "end1")
    (send-text-message camel start "m2")
    (send-text-message camel start "m3")
    (send-text-message camel start "end1")
    (send-text-message camel start "m4")
    (send-text-message camel start "end2")
    (send-text-message camel start "m5")
    (send-text-message camel start "m6")
    (is-message-count end1 3)
    (is-message-count end2 2)
    (is-message-count end-def 6)
    (stop-test camel)))
