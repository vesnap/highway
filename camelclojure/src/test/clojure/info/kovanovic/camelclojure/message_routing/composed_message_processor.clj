(ns info.kovanovic.camelclojure.message-routing.composed-message-processor
  (:use [clojure.test]
        [info.kovanovic.camelclojure.dsl]
	[info.kovanovic.camelclojure.test-util]))
  
(deftest composed-message-processor-pattern
  (let [
	start (direct "start")
	string-input  (direct "string-input")
	integer-input  (direct "integer-input")
	final-output  (direct "final-output")
	aggregator-input (direct "aggregator-input")
	final-output (mock "final-output")
	
	to-lines #(.split %1 "\n")
	
	get-type #(let [type-end (.indexOf %1 ";")
			type-start 5]
		    (.substring %1 type-start type-end))

	get-value #(.substring %1 (+ (.indexOf %1 ";") 7))
	
	
	split-and-route (route (from start)
			       (split to-lines)
			       (router #(= (get-type (first %)) "Integer") integer-input
				       #(= (get-type (first %)) "String")  string-input))

	integer-processing (route (from integer-input)
				  (process #(identity [(* 10 (Integer/parseInt (get-value (first %))))
						       (second %)]))
				  (to aggregator-input))

	string-processing (route (from string-input)
				 (process #(identity [(.toUpperCase (get-value (first %)))
						      (second %)]))
				 (to aggregator-input))

	aggregation-fn (fn [[body1 headers1] [body2 headers2]]
			 (identity [(str body1 body2)
				    headers1]))
	
	aggregate (route (from aggregator-input)
			 (aggregator aggregation-fn "messageId" :count 2)
			 (to final-output))
	
	camel (create split-and-route
		      integer-processing
		      string-processing
		      aggregate)]

    (start-test camel start integer-input string-input aggregator-input final-output)
    (send-text-message camel start "type:String;value:HelloWorld\ntype:Integer;value:10" "messageId" 1)
    (is-message-count final-output 1)
    (is (= (get-received-message final-output) "HELLOWORLD100"))
    (stop-test camel)))
