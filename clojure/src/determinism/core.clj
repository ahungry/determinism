(ns determinism.core
  (:require
   [clojure.tools.logging :as log]
   [clojure.test :as t]
   [clojure.spec.alpha :as s]
   [clojure.spec.gen.alpha :as gen]
   [clojure.spec.test.alpha :as stest]
   [determinism.proxy :as proxy]
   [determinism.stub :as stub]
   )
  (:gen-class))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (log/info "Begin")
  (proxy/all-by-re #"determinism\.stub")
  (stub/main)
  (proxy/record-flush)
  (log/info "End"))
