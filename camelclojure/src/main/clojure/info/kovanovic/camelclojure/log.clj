(ns info.kovanovic.camelclojure.log
  (:import [org.apache.log4j BasicConfigurator]))

(def logging-started (atom false))

(defn start-logging []
  "Start logging if it is not already started."
  (if-not (true? @logging-started)
    (do (BasicConfigurator/configure)
	(reset! logging-started true))))

