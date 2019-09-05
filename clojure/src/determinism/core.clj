(ns determinism.core
  (:require
   [clojure.tools.logging :as log]
   [clojure.test :as t]
   [clojure.spec.alpha :as s]
   [clojure.spec.gen.alpha :as gen]
   [clojure.spec.test.alpha :as stest]
   [determinism.proxy :as proxy]
   [determinism.stub :as stub]
   [determinism.dao :as dao]
   [determinism.replay :as replay]
   [determinism.help :as help]
   )
  (:gen-class))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (log/info "Begin")
  (dao/make-db)
  (proxy/all-by-re #"determinism\.stub")
  (stub/main)
  (proxy/record-flush)
  (log/info "End"))

(defn try-again []
  (replay/replay-all))

(defn get-help []
  (help/get-types #'determinism.stub/javascript-like-plus))

(defn get-help-apropos []
  (help/get-types-apropos "determinism.stub"))
