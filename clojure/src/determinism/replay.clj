(ns determinism.replay
  (:require
   [clojure.test :as t]
   [clojure.repl :refer :all]
   [clojure.tools.logging :as log]
   [determinism.dao :as dao]
   )
  (:gen-class))

(def *report (atom {}))

(defn is-fn? [x]
  (and (var? x)
       (t/function? (deref x))))

(defn is-callable?
  "See if our stringly IDENTIFIER can be called with some coercion."
  [identifier]
  (try
    (let [id (if (string? identifier)
               (read-string identifier)
               identifier)]
      (is-fn? (eval id)))
    (catch Exception e nil)))

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
    (if (or (= out res)
            (= output res))
      (swap! *report update-in [identity :pass] inc-or-one)
      (do
        (prn "Failed on: " out res)
        (swap! *report update-in [identity :fail] inc-or-one))
      )
    ))

(defn execute-previous-inputs [id]
  (->> (dao/search-by-name (str id))
       (map execute-previous-input)
       doall)
  @*report)

(defn replay
  "Given an IDENTIFIER, replay the known inputs and see if they produce
  the same outputs."
  [identifier]
  (when-not (is-callable? identifier)
    (throw (Throwable. "That IDENTIFIER does not exist as a callable IFn!")))
  (execute-previous-inputs identifier))

(defn replay-all
  "Attempt to replay all known and existent functions/symbols."
  []
  (reset! *report {})
  (->> (dao/get-all)
       (map :identity)
       distinct
       (filter is-callable?)
       (map replay)
       doall)
  @*report)
