(ns info.kovanovic.camelclojure.message-routing.delayer
  (:use [clojure.test]
        [info.kovanovic.camelclojure.dsl]
	[info.kovanovic.camelclojure.test-util]))

(deftest delayer-pattern
  (let [start (direct "start")
	end   (mock "end")

	timer (atom nil)
	
	p2 (fn [[body headers]]
	     [(swap! timer #(- %2 %1) (System/currentTimeMillis))
	      headers])
	
	camel (create (route (from start)
			     (process #(identity [(reset! timer (System/currentTimeMillis))
						  (second %)]))
			     (pause 1000)
			     (process p2)
			     (to end)))]
    (start-test camel start end)
    (send-text-message camel start "message")
    (is (and (< @timer 1010) (> @timer 999)))
    (is-message-count end 1)
    (stop-test camel)))
