(ns info.kovanovic.camelclojure.message-routing.routing-slip
  (:use [clojure.test]
        [info.kovanovic.camelclojure.dsl]
	[info.kovanovic.camelclojure.test-util]))
  
(deftest routing-slip-pattern
  (let [start (direct "start")
	camel (create (route (from start)
			     (routing-slip "to")))]

    (.addRouteDefinition camel (route (from "direct:p1")
				      (process #(identity [(.toUpperCase (first %))
							   (second %)]))))
    
    (.addRouteDefinition camel (route (from "direct:p2")
				      (process #(identity [(.substring (first %) 2)
							   (second %)]))))
    (start-test camel start)
    (send-text-message camel start "test" "to" "direct:p1, mock:p3")
    (send-text-message camel start "test" "to" "direct:p1, direct:p2, mock:p3")
    (let [messages (get-received-messages (get-endpoint camel "mock:p3"))]
      (is (= (first messages) "TEST"))
      (is (= (second messages) "ST")))
    (stop-test camel)))
