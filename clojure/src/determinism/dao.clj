(ns determinism.dao
  (:require
   [clojure.tools.logging :as log]
   [clojure.java.jdbc :as jdbc]
   [java-time :as t]
   [cheshire.core :as cheshire]
   )
  (:gen-class))

(def db
  {:classname "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname "../determinism.db"})

(defn time-now []
  (str (t/local-date-time)))

(defn q
  "Wrapper for basic querying, with logging incorporated.
  SS = jdbc interface (vec: sql + args)."
  [ss]
  (let [result (apply jdbc/query db ss)]
    (log/debug ss)
    (log/debug result)
    result))

(defn get-all [] (q ["select * from det"]))

(defn maybe-seq [x]
  (if (seq? x)
    (into [] x)
    x))

(defn serializer [x]
  (->> x
       ;; cheshire/generate-string
       ;; cheshire/parse-string
       maybe-seq
       str))

(defn add [{:keys [identity input input-types output output-type]}]
  (jdbc/insert! db "det"
                {:identity identity
                 :input
                 (serializer input)
                 :input_types
                 (serializer input-types)
                 :output
                 (serializer output)
                 :output_type
                 (serializer output-type)
                 ;; (cheshire/generate-string output-type)
                 :date (time-now)}))
