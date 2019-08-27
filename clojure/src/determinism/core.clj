(ns determinism.core
  (:require
   [clojure.tools.logging :as log]
   )
  (:gen-class))

(def *proxy-records (atom []))

(defn record-flush
  "Dump the records to some storage."
  []
  (log/info @*proxy-records))

(defn proxy-fn
  "Wrap I/O monitoring around a given function."
  [f]
  (fn [& r]
    (let [info {:identity (identity f)
                :input r
                :input-types (map type r)
                :output (apply f r)}]
      (swap! *proxy-records conj info)
      (:output info))))

(defn add-1 [n] (+ 1 n))
(def add-1 (proxy-fn add-1))
(map add-1 (range 2))

(defn summer [{:keys [x y]}] (+ x y))
(def summer (proxy-fn summer))

(map summer [{:x 1 :y 2} {:x 3 :y 4}])

;; (identity (last @*proxy-records))

;; (->> (all-ns) (mapcat ns-publics) (rand-nth) key)

;; (->> (identity *ns*) (ns-publics))

;; (all-ns)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
