(ns determinism.core
  (:require
   [clojure.tools.logging :as log]
   [determinism.dao :as dao]
   )
  (:gen-class))

(def *proxy-records (atom []))

(defn record-flush
  "Dump the records to some storage."
  []
  (doall (map (fn [m]
                (log/info m)
                (dao/add m)) @*proxy-records)))

(defn proxy-fn
  "Wrap I/O monitoring around a given function."
  [f]
  (fn [& r]
    (let [result (apply f r)
          info {:identity (identity f)
                :input r
                :input-types (doall (map str (map type r)))
                :output result
                :output-type (str (type result))}]
      (swap! *proxy-records conj info)
      result)))

(defn add-1 [n] (+ 1 n))
(def add-1 (proxy-fn add-1))
(doall (map add-1 (range 2)))

(defn summer [{:keys [x y]}] (+ x y))
(def summer (proxy-fn summer))

(doall (map summer [{:x 1 :y 2} {:x 3 :y 4}]))

(defn javascript-like-plus
  "Given an X and Y, join strings or add them."
  [x y]
  (if (or (string? x)
          (string? y))
    (str x y)
    (+ x y)))

(def javascript-like-plus (proxy-fn javascript-like-plus))

(do
  (javascript-like-plus 1 2)
  (javascript-like-plus 3 4)
  (javascript-like-plus "x" "y")
  (javascript-like-plus 2 "y")
  (javascript-like-plus "x" 3))

(record-flush)

;; (->> (all-ns) (mapcat ns-publics) (rand-nth) key)

;; (->> (identity *ns*) (ns-publics))

;; (all-ns)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
