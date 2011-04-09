(ns robot-remote-eval.parser
  (:require [clojure.string :as str]))

;; Thanks David: http://www.gettingclojure.com/cookbook:strings
(defn capitalize [s]
  (if (> (count s) 0)
    (str (Character/toUpperCase (.charAt s 0))
         (.toLowerCase (subs s 1)))
    s))

;; Same thanks
(defn capitalize-all [s]
  (let [matcher (re-matcher #"(\w+)" s)
        buffer (new StringBuffer)]
    (while (.find matcher)
      (.appendReplacement matcher buffer (capitalize (.group matcher 1))))
    (.appendTail matcher buffer)
    (.toString buffer)))

(defn rf-to-fn-name
  "Given an RF keyword, derive the function name that RF would send via XML-RPC to our Clojure code"
  [s]
  (-> s
      .toLowerCase
      (str/replace #" " "_")))

(defn fn-name-to-rf
  "The opposite of `rf-to-fn-name`, used for better results output in GUI"
  [s]
  (-> s
      (str/replace #"_" " ")
      capitalize-all))

(defn parse-rf
  "Create a map of function to args pairs based on the code lines passed in"
  [lines]
  (reverse (apply merge
                  (for [line lines]
                    (let [parts (map str/trim (str/split line #"(?<!\\)\|"))]
                      (println line)
                      {(rf-to-fn-name (first parts)) (vec (rest parts))})))))