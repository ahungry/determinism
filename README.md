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
(defn add-1 [n] (+ 1 n))
(map add-1 (range 2))

(defn summer [{:keys [x y]}] (+ x y))
(map summer [{:x 1 :y 2} {:x 3 :y 4}])
```

You will be able to review the results of code execution in a format
such as:

```sh
echo -e '.headers on\n.mode column\n.width 40 20 20 30\nselect * from det;' | sqlite3 determinism.db
identity                                  input                 output                date
----------------------------------------  --------------------  --------------------  ------------------------------
hello_world                                                     Hello World           2019-01-01 12:00:00
bye_world                                                       Goodbye World         2019-01-01 12:00:00
determinism.core$add_1@71e0e636           [0]                   1                     2019-08-27T00:32:58.063620
determinism.core$add_1@71e0e636           [1]                   2                     2019-08-27T00:32:58.076790
determinism.core$summer@419ddc74          [{"x":1,"y":2}]       3                     2019-08-27T00:32:58.092482
determinism.core$summer@419ddc74          [{"x":3,"y":4}]       7                     2019-08-27T00:32:58.105609
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
