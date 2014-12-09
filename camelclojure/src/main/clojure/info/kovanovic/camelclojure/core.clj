(ns info.kovanovic.camelclojure.core
  (:use [info.kovanovic.camelclojure.log :only (start-logging)]
	[clojure.tools.logging :only (trace
					debug
					info
					warn
					error
					fatal)])
  (:import [org.apache.camel Predicate])
  (:import [org.apache.camel.model RouteDefinition])
  (:import [org.apache.camel.impl DefaultCamelContext])
  (:import [org.apache.camel.builder RouteBuilder]))

(defmacro with-message [proc-fn]
  `(fn [ex#]
     (let [body# (.. ex# getIn getBody)
	   headers# (.. ex# getIn getHeaders)]
       (~proc-fn [body#, headers#]))))

(defn create-camel [routes]
  "Creates the apache camel context with the provided routes."
  (start-logging)
  (debug "creating camel context.")
  (let [context (DefaultCamelContext.)
	addroute (fn [c r]
		   (.addRouteDefinition c r)
		   c)]
    (reduce addroute context routes)
    (debug (str "successfully added " (count routes) " routes."))
    context))

(defn start-camel [context]
  "Starts the provided apache camel context."
  (if-not (.isStarted context)
    (.start context)  
    (debug "camel already started."))
  (info "camel started."))

(defn stop-camel [context]
  "Stops the provided apache camel context."
  (if (.isStarted context)
    (.stop context)  
    (debug "camel is not started."))
  (info "camel stopped."))
