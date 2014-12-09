(ns info.kovanovic.camelclojure.message-routing.resequencer
  (:use [clojure.test]
        [info.kovanovic.camelclojure.dsl]
	[info.kovanovic.camelclojure.test-util]))
  
(deftest resequencer-pattern
  (let [start (direct "start")
  	end  (mock "end1")
	r (route (from start)
		 (reorder :batch "order" :size 4)
		 (to end))
  	camel (create r)]
    (start-test camel start end)
    (send-text-message camel start "C" "order" "3")
    (send-text-message camel start "B" "order" "2")
    (send-text-message camel start "D" "order" "4")
    (send-text-message camel start "A" "order" "1")
    (Thread/sleep 1000)
    (is-message-count end 4)
    (is (= (apply str (get-received-messages end)) "ABCD"))
    (stop-test camel)))
