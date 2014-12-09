(ns info.kovanovic.camelclojure.messaging-channels.dead-letter-channel
  (:use [clojure.test]
        [info.kovanovic.camelclojure.dsl]
	[info.kovanovic.camelclojure.test-util]))

(deftest dead-letter-channel-pattern
  (let [start (direct "start")
	end   (mock "end")
	dl    (mock "dead-letter")

	;; function that throws exception if message body is "bad"
	process-fn (fn [[body,headers]]
		     (if (= (str body) "bad")
		       (throw (RuntimeException. "bad message"))))
	
	camel (create (route (from start)
			     (process process-fn)
			     (to end)
			     (dead-letter dl)))]
    
    (start-test camel start end dl)
    (send-text-message camel start "m1")
    (send-text-message camel start "m2")
    (send-text-message camel start "bad")
    (send-text-message camel start "m3")
    (send-text-message camel start "m4")
    (send-text-message camel start "bad")
    (send-text-message camel start "bad")
    (send-text-message camel start "m5")
    (send-text-message camel start "bad")
    (send-text-message camel start "bad")
    (send-text-message camel start "m6")
    (is-message-count end 6)
    (is-message-count dl 5)
    (stop-test camel)))
