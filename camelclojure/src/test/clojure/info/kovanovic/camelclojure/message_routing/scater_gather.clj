(ns info.kovanovic.camelclojure.message-routing.scater-gather
  (:use [clojure.test]
        [info.kovanovic.camelclojure.dsl]
	[info.kovanovic.camelclojure.test-util]))
  
(deftest scater-gather-pattern
  (let [start (direct "start")
	camel (create (route (from start)
			     (recipient-list "sendTo")))
	
	aggregator-input (direct "aggregator-input")
	
	p1-route (route (from (get-direct-endpoint camel "direct://p1-input"))
			(process #(identity [(.toUpperCase (first %))
					     (second %)]))
			(to aggregator-input))

	p2-route (route (from (get-direct-endpoint camel "direct://p2-input"))
			(process #(identity [(.toLowerCase (first %))
					     (second %)]))
			(to aggregator-input))
	
	p3-route (route (from (get-direct-endpoint camel "direct://p3-input"))
			(process #(identity [(.length (first %))
					     (second %)]))
			(to aggregator-input))
	
	p4-route (route (from (get-direct-endpoint camel "direct://p4-input"))
			(process #(identity [(apply str (reverse (first %)))
					      (second %)]))
			(to aggregator-input))

	final-output (mock "final")

	aggregation-fn (fn [[body1 headers1] [body2 headers2]]
			 (identity [(str body1 body2)
				    headers1]))
	
	aggregate-results (route (from aggregator-input)
				 (aggregator aggregation-fn "sendTo" :count 4)
				 (to final-output))]
    (.addRouteDefinition camel p1-route)
    (.addRouteDefinition camel p2-route)
    (.addRouteDefinition camel p3-route)
    (.addRouteDefinition camel p4-route)
    (.addRouteDefinition camel aggregate-results)
    (start-test camel start aggregator-input final-output)
    (send-text-message camel start "Message" "sendTo" "direct://p1-input,direct://p2-input,direct://p3-input,direct://p4-input")
    (is (= (get-received-message final-output)
	   "MESSAGEmessage7egasseM"))
    (stop-test camel)))
