(ns info.kovanovic.camelclojure.system-management.detour
  (:use [clojure.test]
        [info.kovanovic.camelclojure.dsl]
	[info.kovanovic.camelclojure.test-util]))

(deftest detour-pattern
  (let [start (direct "start")
	upper-case-input (direct "upper-case-input")
	end (mock "end")

	camel (create (route (from start)
			     (router #(= (first %) "uppercase") upper-case-input)
			     (to end))

		      (route (from upper-case-input)
			     (process #(identity [(.toUpperCase (str (first %)))
						   (second %)]))))]

    (start-test camel start end upper-case-input)

    (send-text-message camel start "uppercase")
    (send-text-message camel start "normal")

    (let [messages (get-received-messages end)]
      (is (= (first messages) "UPPERCASE"))
      (is (= (second messages) "normal"))) 
    (stop-test camel)))
