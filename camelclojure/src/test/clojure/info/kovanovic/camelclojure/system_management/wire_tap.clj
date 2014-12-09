(ns info.kovanovic.camelclojure.system-management.wire-tap
  (:use [clojure.test]
        [info.kovanovic.camelclojure.dsl]
	[info.kovanovic.camelclojure.test-util]))

(deftest wire-tap-pattern
  (let [start (direct "start")
	end (mock "end")
	camel (create (route (from start)
			     (wire-tap "mock:wire-tap-in")
			     (to end)))]
    (start-test camel start end)
    (send-text-message camel start "m1")
    (send-text-message camel start "m2")
    (send-text-message camel start "m3")
    (is-message-count end 3)
    (is-message-count (get-endpoint camel "mock:wire-tap-in") 3)
    (stop-test camel)))
