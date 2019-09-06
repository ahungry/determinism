(ns determinism.stub
  (:require
   [clojure.tools.logging :as log]
   [determinism.proxy :as proxy]
   )
  (:gen-class))

(defn javascript-like-plus
  "Given an X and Y, join strings or add them."
  [x y]
  (if (or (string? x)
          (string? y))
    (str x y)
    (+ x y)))

;; "A prime example of data chaining pitfall - it passes data along, but what data?
;;  Maybe not relevant at this point in time, but it could be in the future."
(defn foo-map [m]
  (update-in m [:foo :bar] inc))

(defn bar-map [m]
  (-> (foo-map m)
      (update-in [:foo :bar] inc)))

(defn hof-sample [f g]
  (fn [x] (f (g x))))

(defn main []
  (javascript-like-plus 1 2)
  (javascript-like-plus 3 4)
  (javascript-like-plus "x" "y")
  (javascript-like-plus 2 "y")
  (javascript-like-plus "x" 3)
  (hof-sample inc inc)
  (bar-map {:foo {:bar 5 :baz 9}}))
