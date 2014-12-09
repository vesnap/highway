(ns info.kovanovic.camelclojure.system-management.log
  (:use [clojure.test]
        [info.kovanovic.camelclojure.dsl]
	[info.kovanovic.camelclojure.test-util]))

(deftest log-pattern
  (let [start (direct "start")
	end  (mock "end")

	camel (create (route (from start)
			     (process #(do (println "Processing" (first %))
					   (identity %)))
			     (to end)))]
    (start-test camel start end)
    (send-text-message camel start "m1")
    (send-text-message camel start "m2")
    (send-text-message camel start "m3")
    (is-message-count end 3)
    (stop-test camel)))
