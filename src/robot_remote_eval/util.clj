(ns robot-remote-eval.util)

(defmacro with-action
  "Macro to add action listeners to Swing components"
  [component event & body]
  `(. ~component addActionListener
      (proxy [java.awt.event.ActionListener] []
        (actionPerformed [~event] ~@body))))