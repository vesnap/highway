(ns info.kovanovic.camelclojure.messaging-systems.filter
  (:use [clojure.test]
        [info.kovanovic.camelclojure.dsl]
	[info.kovanovic.camelclojure.test-util]))

(deftest filter-pattern
  (let [start (direct "start")
	end   (mock "end")

	camel (create (route (from start)
			     (extract #(not (= (first %) "filtered")))
			     (to end)))]
    (start-test camel start end)
    (send-text-message camel start "filtered")
    (send-text-message camel start "text1")
    (send-text-message camel start "filtered")
    (send-text-message camel start "filtered")
    (send-text-message camel start "text2")
    (send-text-message camel start "text3")
    (send-text-message camel start "filtered")
    (is-message-count end 3)
    (stop-test camel)))
