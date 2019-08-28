(ns determinism.proxy
  (:require
   [clojure.tools.logging :as log]
   [clojure.test :as t]
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

(defn all-by-re [re]
  (->> (scoper/everything-filtered re)
       (map proxy-def)
       doall
       ))
