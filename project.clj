(defproject robot-remote-eval "0.1.0"
  :description "A Robot Framework keyword evaluator for remote libraries"
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [necessary-evil "1.1.0"]
                 [com.miglayout/miglayout "3.7.4"]]
  :dev-dependencies [[swank-clojure "1.3.0"]]
  :main robot-remote-eval.core)
