(ns info.kovanovic.camelclojure.dsl
  (:require [info.kovanovic.camelclojure.impl.aggregator     :as ag]
	    [info.kovanovic.camelclojure.impl.dead-letter    :as dl]
	    [info.kovanovic.camelclojure.impl.extract        :as ex]
	    [info.kovanovic.camelclojure.impl.from           :as fr]
	    [info.kovanovic.camelclojure.impl.loop           :as lo]
	    [info.kovanovic.camelclojure.impl.pause          :as pa]
	    [info.kovanovic.camelclojure.impl.process        :as pc]
	    [info.kovanovic.camelclojure.impl.predicate      :as pr]
	    [info.kovanovic.camelclojure.impl.router         :as rt]
	    [info.kovanovic.camelclojure.impl.reorder        :as re]
	    [info.kovanovic.camelclojure.impl.recipient-list :as rl]
	    [info.kovanovic.camelclojure.impl.routing-slip   :as rs]
	    [info.kovanovic.camelclojure.impl.throttle       :as th]
	    [info.kovanovic.camelclojure.impl.load-balancer  :as lb]
	    [info.kovanovic.camelclojure.impl.split          :as sp]
	    [info.kovanovic.camelclojure.impl.sample         :as sa]
	    [info.kovanovic.camelclojure.impl.to             :as t2]
	    [info.kovanovic.camelclojure.impl.wire-tap       :as wt]
	    [info.kovanovic.camelclojure.core                :as co])
  (:import (org.apache.camel.builder Builder)
	   (org.apache.camel.model RouteDefinition)))

(defmacro route [& stmnts]
  `(let [rd# (RouteDefinition.)]
     (-> rd# ~@stmnts)
     rd#))

(defn start [context]
  (co/start-camel context))

(defn stop [context]
  (co/stop-camel context))

(defn create [& routes]
  (co/create-camel routes))

(defmacro with-message [f]
  (co/with-message f))

(defn aggregator [r fn h & opts]
  (ag/aggregator r fn h opts))

(defn dead-letter [r url & opts]
  (dl/dead-letter r url opts))

(defn extract [r f]
  (ex/extract r f))

(defn from [r url & opts]
  (fr/from r url opts))

(defn pause [r milis]
  (pa/pause r milis))

(defn predicate [f]
  (pr/predicate f))

(defn router [r & args]
 (rt/router r args))

(defn throttle [r c & opts]
  (th/throttle r c opts))

(defn process [r f]
  (pc/process r f))

(defn processor [f]
  (pc/processor f))

(defn split [r f]
  (sp/split r f))

(defn sample [r & opts]
  (sa/sample r opts))

(defn doloop [r & opts]
  (lo/doloop r opts))

(defn to [r & args]
  (t2/to r args))

(defn routing-slip [r h & args]
  (rs/routing-slip r h args))

(defn reorder [r t h & opts]
  (re/reorder r t h opts))

(defn recipient-list [r h & opts]
  (rl/recipient-list r h opts))

(defn wire-tap [r url]
  (wt/wire-tap r url))

(defn load-balancer [r t & opts]
  (lb/load-balancer r t opts))

(defn header [hdr next]
  (.. Builder (header hdr) next))

(defn body [hdr next]
  (.. Builder (header hdr) next))
