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

```clojure
[{:identity #<Fn@74a23675 determinism.core/add_1>,
  :input (0),
  :input-types (#<Class@649d209a java.lang.Long>),
  :output 1}
 {:identity #<Fn@74a23675 determinism.core/add_1>,
  :input (1),
  :input-types (#<Class@649d209a java.lang.Long>),
  :output 2}
 {:identity #<Fn@2391f319 determinism.core/summer>,
  :input ({:x 1, :y 2}),
  :input-types (#<Class@1d251891 clojure.lang.PersistentArrayMap>),
  :output 3}
 {:identity #<Fn@2391f319 determinism.core/summer>,
  :input ({:x 3, :y 4}),
  :input-types (#<Class@1d251891 clojure.lang.PersistentArrayMap>),
  :output 7}]
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
