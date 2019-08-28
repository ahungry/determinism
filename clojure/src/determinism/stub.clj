(ns determinism.stub
  (:require
   [clojure.tools.logging :as log]
   )
  (:gen-class))

(defn javascript-like-plus
  "Given an X and Y, join strings or add them."
  [x y]
  (if (or (string? x)
          (string? y))
    (str x y)
    (+ x y)))

(defn main []
  (javascript-like-plus 1 2)
  (javascript-like-plus 3 4)
  (javascript-like-plus "x" "y")
  (javascript-like-plus 2 "y")
  (javascript-like-plus "x" 3))
