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
   identity = determinism.stub$javascript_like_plus@17796487
      input = [1,2]
input_types = ["class java.lang.Long","class java.lang.Long"]
     output = 3
output_type = "class java.lang.Long"
       date = 2019-08-28T00:46:25.672520

   identity = determinism.stub$javascript_like_plus@17796487
      input = [3,4]
input_types = ["class java.lang.Long","class java.lang.Long"]
     output = 7
output_type = "class java.lang.Long"
       date = 2019-08-28T00:46:25.825044

   identity = determinism.stub$javascript_like_plus@17796487
      input = ["x","y"]
input_types = ["class java.lang.String","class java.lang.String"]
     output = "xy"
output_type = "class java.lang.String"
       date = 2019-08-28T00:46:25.834660

   identity = determinism.stub$javascript_like_plus@17796487
      input = [2,"y"]
input_types = ["class java.lang.Long","class java.lang.String"]
     output = "2y"
output_type = "class java.lang.String"
       date = 2019-08-28T00:46:25.844010

   identity = determinism.stub$javascript_like_plus@17796487
      input = ["x",3]
input_types = ["class java.lang.String","class java.lang.Long"]
     output = "x3"
output_type = "class java.lang.String"
       date = 2019-08-28T00:46:25.853266

   identity = determinism.stub$main@465ead
      input = null
input_types = []
     output = "x3"
output_type = "class java.lang.String"
       date = 2019-08-28T00:46:25.871784
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
