(ns info.kovanovic.camelclojure.messaging-channels.point-to-point-channel
  (:use [clojure.test]
        [info.kovanovic.camelclojure.dsl]
	[info.kovanovic.camelclojure.test-util]))
  
(deftest point-to-point-channel-pattern
  (let [start (direct "start")
	end   (mock "end")
	camel (create (route (from start)
		             (to end)))]
    
    (start-test camel start end)
    (send-text-message camel start "message 1")
    (send-text-message camel start "message 2")
    (is-message-count end 2)
    (stop-test camel)))
