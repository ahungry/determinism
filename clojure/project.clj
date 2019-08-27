(defproject determinism "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [
                 ;; Language related
                 [org.clojure/clojure "1.10.0"]
                 [clojure.java-time "0.3.2"]

                 ;; Database related
                 [org.clojure/java.jdbc "0.3.5"]
                 [org.xerial/sqlite-jdbc "3.7.2"]

                 ;; Parsing related
                 [cheshire "5.9.0"]

                 ;; Logging related
                 [ch.qos.logback/logback-classic "1.2.3"]
                 [org.clojure/tools.logging "0.5.0"]
                 ]
  :main ^:skip-aot determinism.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
