# determinism

> Determinism is the philosophical belief that all events are determined
> completely by previously existing causes.

This aims to be a development tool for programming, where a code base
may be targetted for monitoring functions, and as each function is
hit, it will record the data the function received (input) as well as
the output the function generated.

# Idea

Given some functions defined as such:

```clojure
(defn javascript-like-plus
  "Given an X and Y, join strings or add them."
  [x y]
  (if (or (string? x)
          (string? y))
    (str x y)
    (+ x y)))
```

You will be able to review the results of code execution in a format
such as:

```sh
echo -e '.headers on\n.mode column\n.width 37 20 54 10 30\nselect * from det limit 5 offset 6;' | sqlite3 determinism.db
identity                               input                 input_types                                             output      output_type                     date
-------------------------------------  --------------------  ------------------------------------------------------  ----------  ------------------------------  --------------------------
determinism.core$javascript_like_plus  [1,2]                 ["class java.lang.Long","class java.lang.Long"]         3           "class java.lang.Long"          2019-08-27T00:46:35.388919
determinism.core$javascript_like_plus  [3,4]                 ["class java.lang.Long","class java.lang.Long"]         7           "class java.lang.Long"          2019-08-27T00:46:35.408665
determinism.core$javascript_like_plus  ["x","y"]             ["class java.lang.String","class java.lang.String"]     "xy"        "class java.lang.String"        2019-08-27T00:46:35.425899
determinism.core$javascript_like_plus  [2,"y"]               ["class java.lang.Long","class java.lang.String"]       "2y"        "class java.lang.String"        2019-08-27T00:46:35.436628
determinism.core$javascript_like_plus  ["x",3]               ["class java.lang.String","class java.lang.Long"]       "x3"        "class java.lang.String"        2019-08-27T00:46:35.450455
```

# Why?

Such a feature would allow for replaying of previous inputs against a
code base which changed the individual implementation of some
functions, as well as allowing a function to provide additional
information regarding expected types and outputs (even if not using a
statically typed language), as the function signatures will be able to
be displayed based on the real data received at runtime, not promises
the developer has made to the compiler.

## Copyright

Copyright © 2019 Matthew Carter <m@ahungry.com>

# License

Distributed under the GNU Affero General Public License either version 3.0 or (at
your option) any later version.
