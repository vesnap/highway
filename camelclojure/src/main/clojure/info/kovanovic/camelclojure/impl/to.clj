(ns info.kovanovic.camelclojure.impl.to)

(defn- add-destinations [r dests]
  (if-not (empty? dests)
    (recur (.to r (first dests))
	   (rest dests))
    r))

(defn to [r dests]
  (cond 
   (= (first dests) :multicast)
   (let [multicast-route (add-destinations (.multicast r) (rest dests))]
     (.end multicast-route))

   (= (first dests) :pipeline)
   (add-destinations (.pipeline r) (rest dests))
   
   true
   (add-destinations r dests)))
