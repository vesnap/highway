(ns info.kovanovic.camelclojure.test-util
  (:use info.kovanovic.camelclojure.dsl
	clojure.test)
  (:import [org.apache.camel.component.mock MockEndpoint]
	   [org.apache.camel.component.direct DirectEndpoint]
	   [org.apache.camel ProducerTemplate]
    [org.apache.camel.component.direct DirectComponent]))

(defn directComponent []
  (DirectComponent. ))


(defn mock [url]
  (MockEndpoint. (str "mock://" url)))

(defn direct [url]
  (DirectEndpoint. (str "direct://" url) (directComponent)))

(defn- init [context endpoints]
  (if-not (empty? endpoints)
    (do (.setCamelContext (first endpoints) context)
	(recur context (rest endpoints)))))

(defn start-test [camel & endpoints]
  (init camel endpoints)
  (start camel))

(defn stop-test [camel]
  (stop camel))

(defn publish [camel endpoint processor-fn]
  (.. camel createProducerTemplate (send endpoint (processor processor-fn))))

(defn get-received-messages [endpoint]
  (map #(.. % getIn getBody) (.getReceivedExchanges endpoint)))

(defn get-received-message [endpoint]
  (first (get-received-messages endpoint)))

(defn get-endpoint [context endpoint-name]
  (.getEndpoint context endpoint-name MockEndpoint))

(defn get-direct-endpoint [context endpoint-name]
  (.getEndpoint context endpoint-name DirectEndpoint))

(defn send-text-message [camel endpoint message & headers]
  (publish camel endpoint (fn [[m-body m-headers]]
			    (if-not (nil? headers)
			      [message {(first headers)
					(second headers)}]
			      [message m-headers]))))



(defn count-messages [endpoint]
  (.getReceivedCounter endpoint))

(defn join-bodies [ex1 ex2]
  (do (if-not (nil? ex1)
	(let [ex1-body (.. ex1 getIn getBody)
	      ex2-body (.. ex2 getIn getBody)]
	  (.. ex2 getIn (setBody (str ex1-body ex2-body)))))
      ex2))

(defn is-message-count [endpoint count]
  (is (= (count-messages endpoint) count)))


;;; tmp

;; (defn filename-contains-fn-creator [name-part]
;;   (fn [exchange]
;;     (.. exchange getIn getBody toString (contains name-part))))

;; (defn smor-ex [arg]
;;   (throw (Exception. "smor")))
 
;; (def proc1c (atom 0))

;; (defn proc1 [arg]
;;   (swap! proc1c inc)
;;   (debug (str "processing called" + @proc1c))
;;   (if (< (. Math random) 0.5)
;;     (do (debug "fail.")
;; 	(throw (Exception. "smor")))
;;     (debug (str "success. after: " @proc1c))))
