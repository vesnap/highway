(ns info.kovanovic.camelclojure.message-routing.dynamic-router
  (:use [clojure.test]
        [info.kovanovic.camelclojure.dsl]
	[info.kovanovic.camelclojure.test-util]))

(deftest dynamic-router-pattern
  (let [start (direct "start")
	end1  (mock "end1")
	end-def (mock "end-def")

	camel (create (route (from start)
			     (router (fn [_]
				       (odd? (System/currentTimeMillis)))
				     end1
				     end-def)))]

    (start-test camel start end1 end-def)

    (send-text-message camel start "message1")
    (send-text-message camel start "message2")

    (is (= (+ (count-messages end1)
	      (count-messages end-def))
	   2)) 
    (stop-test camel)))
