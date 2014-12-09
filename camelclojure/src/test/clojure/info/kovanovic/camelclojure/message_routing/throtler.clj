(ns info.kovanovic.camelclojure.message-routing.throtler
  (:use [clojure.test]
        [info.kovanovic.camelclojure.dsl]
	[info.kovanovic.camelclojure.test-util]))
  
(deftest throtler-pattern
  (let [start (direct "start")
	end (mock "end")
	message-count 10
	bucket 2
	camel (create (route (from start)
			     (throttle bucket :period-millis 1000)
			     (to end)))
	time (atom (System/currentTimeMillis))]
    (start-test camel start end)

    (dotimes [_ message-count]
      (send-text-message camel start "m"))
    (swap! time #(- %2 %1) (System/currentTimeMillis))

    (is (and (< @time (* (/ message-count bucket) 1000))
	     (> @time (* (dec (/ message-count bucket)) 1000))))
    (stop-test camel)))
