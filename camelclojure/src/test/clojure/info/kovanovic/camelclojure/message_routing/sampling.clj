(ns info.kovanovic.camelclojure.message-routing.sampling
  (:use [clojure.test]
        [info.kovanovic.camelclojure.dsl]
	[info.kovanovic.camelclojure.test-util]))
  
(deftest sampling-pattern
  (let [start (direct "start")
	sample-output  (mock "sample-output")
	
	camel (create (route (from start)
			     (sample :every 3)
			     (to sample-output)))]

    (start-test camel start sample-output)
    (dotimes [i 24]
      (send-text-message camel start (str "m" i)))
    (is-message-count sample-output 8)
    (stop-test camel)))
