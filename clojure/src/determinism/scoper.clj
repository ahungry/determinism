(ns determinism.scoper
  (:require
   [clojure.tools.logging :as log]
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

(defn everything-filtered
  "Usually you would want just your project symbols, so calling with RE of:
  `determinism\\..*?/' for instance will match all symbols in this project,
  and rarely any else."
  [re]
  (filter (re-vs-symbol re) (everything)))

(defn active-ns
  "Get the symbols that are just in this namespace (well, the active one anyways)."
  []
  (->> *ns* (ns-publics)))
