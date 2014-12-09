(ns info.kovanovic.camelclojure.message-routing.aggregator
  (:use [clojure.test]
        [info.kovanovic.camelclojure.dsl]
	[info.kovanovic.camelclojure.test-util]))
  
(deftest aggregator-pattern
  (let [start (direct "start")
	end  (mock "end")
	f (fn [[body1 headers1] [body2 headers2]]
	    (identity [(str body1 body2)
		       headers1]))

	r (route (from start)
		 (aggregator f "type" :count 2)
		 (to end))
	camel (create r)]
    (start-test camel start end)
    (send-text-message camel start "Java" "type" "t1")
    (send-text-message camel start "Clojure" "type" "t2")
    (send-text-message camel start "Eclipse" "type" "t1")
    (send-text-message camel start "Emacs" "type" "t2")
    
    (let [messages (get-received-messages end)]
      (is (= (count messages) 2))
      (is (= (first messages) "JavaEclipse"))
      (is (= (second messages) "ClojureEmacs")))
    (stop-test camel)))
