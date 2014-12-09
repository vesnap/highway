(ns info.kovanovic.camelclojure.message-routing.receipent-list
  (:use [clojure.test]
        [info.kovanovic.camelclojure.dsl]
	[info.kovanovic.camelclojure.test-util]))
  
(deftest static-receipent-list-pattern
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

(deftest dynamic-receipent-list-pattern
  (let [start (direct "start")
	end1  (mock "end1")
	end2  (mock "end2")
	camel (create (route (from start)
			     (recipient-list
			      "sendTo"
			      :paralel
			      :timeout 1000)))]
    (start-test camel start end1 end2)
    (send-text-message camel start "m1" "sendTo" "mock:end1, mock:end2")
    (send-text-message camel start "m2" "sendTo" "mock:end1")
    (send-text-message camel start "m3" "sendTo" "mock:end1")
    (send-text-message camel start "m4" "sendTo" "mock:end1")
    (send-text-message camel start "m5" "sendTo" "mock:end1")
    (send-text-message camel start "m6" "sendTo" "mock:end1, mock:end2")
    (send-text-message camel start "m7" "sendTo" "mock:end2")

    (is-message-count (get-endpoint camel "mock:end1") 6)
    (is-message-count (get-endpoint camel "mock:end2") 3)
    (stop-test camel)))
