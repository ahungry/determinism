(ns determinism.scoper
  (:require
   [clojure.tools.logging :as log]
   [clojure.test :as t]
   )
  (:gen-class))

(defn everything
  "Get all the symbols we can find."
  []
  (->> (all-ns)
       (mapcat ns-publics)
       ))

(defn re-vs-symbol
  "Filter regex vs a symbol."
  [re]
  (fn [s]
    (re-find re (str s))))

(defn inner-fn? [x]
  (and (= clojure.lang.Var (type x))
       (t/function? (deref x))))

(defn everything-filtered
  "Usually you would want just your project symbols, so calling with RE of:
  `determinism\\..*?/' for instance will match all symbols in this project,
  and rarely any else."
  [re]
  (->> (filter (re-vs-symbol re) (everything))
       (map #(get % 1))
       (filter inner-fn?)))

(defn active-ns
  "Get the symbols that are just in this namespace (well, the active one anyways)."
  []
  (->> *ns* (ns-publics)))
