(ns determinism.core
  (:require
   [clojure.tools.logging :as log]
   [clojure.test :as t]
   [clojure.spec.alpha :as s]
   [clojure.spec.gen.alpha :as gen]
   [clojure.spec.test.alpha :as stest]
   [determinism.dao :as dao]
   [determinism.scoper :as scoper]
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

;; TODO: Get this global wrapper code working...pretty close I think.
(defn proxy-def-x
  "Redefine a function with the wrapper around it. k = Symbol, v = Var to Fn."
  [[k v]]
  (binding [*ns* 'determinism.dao]
    (eval
     `(def ~k ~(proxy-fn (deref v))))))

(defn proxy-def
  "Redefine a function with the wrapper around it. k = Symbol, v = Var to Fn."
  [v]
  (prn "Wrapping for determinism: " (identity v))
  (alter-var-root v proxy-fn))

(defn inner-fn? [x]
  (and (= clojure.lang.Var (type x))
       (t/function? (deref x))))

(defn proxy-all-by-re [re]
  (->> (scoper/everything-filtered re)
       (map #(get % 1))
       (filter inner-fn?)
       (map proxy-def)
       doall
       ))

(proxy-all-by-re #"determinism")

(defn add-1 [n] (+ 1 n))

(proxy-def ['add-1 #'determinism.core/add-1])

(proxy-def ['add #'determinism.dao/add])

;; (def add-1 (proxy-fn add-1))
(doall (map add-1 (range 2)))

(defn summer [{:keys [x y]}] (+ x y))
;; (def summer (proxy-fn summer))

(doall (map summer [{:x 1 :y 2} {:x 3 :y 4}]))

(defn javascript-like-plus
  "Given an X and Y, join strings or add them."
  [x y]
  (if (or (string? x)
          (string? y))
    (str x y)
    (+ x y)))

;; (def javascript-like-plus (proxy-fn javascript-like-plus))

(do
  (javascript-like-plus 1 2)
  (javascript-like-plus 3 4)
  (javascript-like-plus "x" "y")
  (javascript-like-plus 2 "y")
  (javascript-like-plus "x" 3))

(record-flush)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
