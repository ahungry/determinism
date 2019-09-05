(ns determinism.help
  (:require
   [clojure.test :as t]
   [clojure.repl :refer :all]
   [clojure.tools.logging :as log]
   [determinism.dao :as dao]
   )
  (:gen-class))


(defn get-types
  "Pull in the available type information for a symbol."
  [x]
  (->> (dao/get-by-identity (str x))
       (map :input_types)
       (map read-string)
       distinct
       sort))

(defn get-types-with-info [x]
  {:identity x
   :types (get-types x)})

(defn get-types-apropos
  "Do a loose search on some input/keyword and get all found type info."
  [x]
  (->> (dao/apropos-identities (str x))
       (map get-types-with-info)))
