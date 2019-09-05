# determinism

> Determinism is the philosophical belief that all events are determined
> completely by previously existing causes.

This aims to be a development tool for programming, where a code base
may be targetted for monitoring functions, and as each function is
hit, it will record the data the function received (input) as well as
the output the function generated.

# Idea

Given you had a codebase defined as such:

```clojure
(ns determinism.stub)

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
```

and you wrapped it in this way:

```clojure
  (proxy/all-by-re #"determinism\.stub")
  (stub/main)
  (proxy/record-flush)
```

You will be able to review the results of code execution in a format
such as:

```sh
identity                                  input       input_types                         output      output_type               date
----------------------------------------  ----------  ----------------------------------  ----------  ------------------------  --------------------------
#'determinism.stub/javascript-like-plus   [1 2]       [java.lang.Long java.lang.Long]     3           class java.lang.Long      2019-09-01T00:48:03.820992
#'determinism.stub/javascript-like-plus   [3 4]       [java.lang.Long java.lang.Long]     7           class java.lang.Long      2019-09-01T00:48:03.894416
#'determinism.stub/javascript-like-plus   ["x" "y"]   [java.lang.String java.lang.String  xy          class java.lang.String    2019-09-01T00:48:03.903399
#'determinism.stub/javascript-like-plus   [2 "y"]     [java.lang.Long java.lang.String]   2y          class java.lang.String    2019-09-01T00:48:03.912170
#'determinism.stub/javascript-like-plus   ["x" 3]     [java.lang.String java.lang.Long]   x3          class java.lang.String    2019-09-01T00:48:03.921038
```

# Features

## Replayability

In the codebase, you can "replay" previous results and ensure the
I/O matches known I/O as such:

```clojure
(require '[determinism.replay :as replay])
(replay/replay-all)

;; Evaluates to:
{"#'determinism.stub/bar-map" {:pass 1},
 "#'determinism.stub/foo-map" {:pass 1},
 "#'determinism.stub/javascript-like-plus" {:pass 5},
 "#'determinism.stub/main" {:pass 1}}
```

## Inspection

You can use the help module to pull in more information the system has
recorded or knows about your data:

```clojure
(require '[determinism.help :as help])
(help/get-types #'determinism.stub/javascript-like-plus)

;; Will evaluate to (all the known input combinations of types):
([java.lang.Long java.lang.Long]
 [java.lang.Long java.lang.String]
 [java.lang.String java.lang.Long]
 [java.lang.String java.lang.String])
```

You can also pull in a wide set of type information based on a regex
query for matching functions:

```clojure
(help/get-types-apropos "determinism.stub")

;; Evals to:
({:identity "#'determinism.stub/javascript-like-plus",
  :types ([java.lang.Long java.lang.Long]
          [java.lang.Long java.lang.String]
          [java.lang.String java.lang.Long]
          [java.lang.String java.lang.String])}
 {:identity "#'determinism.stub/foo-map",
  :types ([clojure.lang.PersistentArrayMap])}
 {:identity "#'determinism.stub/bar-map",
  :types ([clojure.lang.PersistentArrayMap])}
 {:identity "#'determinism.stub/main", :types ([])})
```


# Why?

Such a feature would allow for replaying of previous inputs against a
code base which changed the individual implementation of some
functions, as well as allowing a function to provide additional
information regarding expected types and outputs (even if not using a
statically typed language), as the function signatures will be able to
be displayed based on the real data received at runtime, not promises
the developer has made to the compiler.

# todo

- Implement replay feature

## Copyright

Copyright © 2019 Matthew Carter <m@ahungry.com>

# License

Distributed under the GNU Affero General Public License either version 3.0 or (at
your option) any later version (AGPLv3).

See [LICENSE.txt](https://github.com/ahungry/determinism/blob/master/LICENSE.txt) for details and exceptions.
