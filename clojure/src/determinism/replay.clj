(ns determinism.replay
  (:require
   [clojure.test :as t]
   [clojure.repl :refer :all]
   [clojure.tools.logging :as log]
   [xdg-rc.core :as xdg-rc]
   [determinism.dao :as dao]
   )
  (:gen-class))

(def *report (atom {}))

(defn is-fn? [x]
  (and (var? x)
       (t/function? (deref x))))

(defn inc-or-one [n] (if (nil? n) 1 (inc n)))

(defn read-string-or-not [s]
  (try (read-string s)
       (catch Exception e s)))

;; TODO: Add an interactive strategy to swap old input saved with new input.
;; TODO: Do not do the read-string if the value type for a slot was in fact string.
(defn execute-previous-input
  "Given a previous record, re-run the inputs and ensure the output
  remains the same as it did in a prior evocation."
  [{:keys [identity input output]}]
  (let [in (read-string-or-not input)
        out (read-string-or-not output)
        id (read-string identity)
        f (deref (eval id))
        res (apply f in)]
    (if (= out res)
      (swap! *report update-in [identity :pass] inc-or-one)
      (do
        (prn "Failed on: " out res)
        (swap! *report update-in [identity :fail] inc-or-one))
      )
    ))

(defn execute-previous-inputs [id]
  (reset! *report {})
  (->> (dao/search-by-name (str id))
       (map execute-previous-input)
       )
  ;; @*report
  )

(defn replay
  "Given an IDENTIFIER, replay the known inputs and see if they produce
  the same outputs."
  [identifier]
  (let [id (if (string? identifier)
             (read-string identifier)
             identifier)]
    (when-not (is-fn? id)
      (throw (Throwable. "That IDENTIFIER does not exist!")))
    (prn "here..")
    (execute-previous-inputs identifier)))