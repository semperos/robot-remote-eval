(ns robot-remote-eval.history
  (:require [clojure.contrib.seq-utils :as su]))

(def session-history (atom []))

(defn save-to-history
  "Given code from the text area, add it to the entries in the session-history"
  [s]
  (swap! session-history conj s))

(defn output-history
  []
  (apply str
         (for [[idx entry] (su/indexed @session-history)]
           (str (inc idx) ":\n" entry "\n\n"))))