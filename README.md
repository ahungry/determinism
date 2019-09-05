# determinism

NOTE: This is ALPHA level software.  It's available to garner interest
and such, but please understand there is a lot more to do on this.

[![Clojars Project](https://img.shields.io/clojars/v/ahungry/determinism.svg)](https://clojars.org/ahungry/determinism)
[![cljdoc badge](https://cljdoc.org/badge/ahungry/determinism)](https://cljdoc.org/d/ahungry/determinism)

> Determinism is the philosophical belief that all events are determined
> completely by previously existing causes.

This aims to be a development tool for programming, where a code base
may be targetted for monitoring functions, and as each function is
hit, it will record the data the function received (input) as well as
the output the function generated.

# Installation

Just add into your project.clj or deps:

[![Clojars Project](http://clojars.org/ahungry/determinism/latest-version.svg)](http://clojars.org/ahungry/determinism)

Then when you want to use it, import it as:

```clojure
(ns your-package
  (:require [determinism.help :as help]
            [determinism.dao :as dao]
            [determinism.proxy :as proxy]
            [determinism.replay :as replay]))
```

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

This should end up acting as a live-check equivalent to unit testing
or spec based approaches (the more you instrument your code and send
the results to the database at ~/.local/share/ahungry-determinism.db,
the more the system can assist in regression-proofing and enabling a
faster development workflow).

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

# License

Copyright © 2019 Matthew Carter <m@ahungry.com>

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
