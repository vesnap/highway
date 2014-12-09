(ns info.kovanovic.camelclojure.messaging-systems.message-translator
  (:use [clojure.test]
        [info.kovanovic.camelclojure.dsl]
	[info.kovanovic.camelclojure.test-util]))

(deftest message-translator-pattern
  (let [start (direct "start")
	end   (mock "end")

	camel (create (route (from start)
				   (process #(identity [(.toUpperCase (first %))
							(second %)]))
				   (to end)))]
    (start-test camel start end)
    (send-text-message camel start "message")
    (is (= "MESSAGE" (get-received-message end)))
    (stop-test camel)))
