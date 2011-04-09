;; This is a GUI "REPL" for RobotFramework keywords
;; defined in the robot-framework-clowd project

(ns robot-remote-eval.core
  (:use [robot-remote-eval history parser util]
        [necessary-evil.core :only [call]])
  (:require [clojure.string :as str])
  (:import [javax.swing
            JFrame JPanel JButton JTextArea
            JScrollPane JLabel SwingConstants
            Popup JSeparator SwingWorker]
           [java.awt Color Toolkit]
           [java.awt.datatransfer Clipboard StringSelection]
           net.miginfocom.swing.MigLayout)
  (:gen-class
   :main true))

;; Text area where code can be written. Button to evaluate code. Button to clear text area.
;; Save session history of commands (text area) so they can go back and reuse.

;; ## Component Definitions
(def lbl-code-area (JLabel. "Keywords"))
(def textarea-code (doto (JTextArea. 200 400)
                        (.setLineWrap true)))
(def scrollpane-code (JScrollPane.
                      textarea-code
                      JScrollPane/VERTICAL_SCROLLBAR_ALWAYS
                      JScrollPane/HORIZONTAL_SCROLLBAR_NEVER))

(def lbl-results-area (JLabel. "Results"))
(def textarea-results (doto (JTextArea. 200 400)
                           (.setLineWrap true)
                           (.setEditable false)))
(def scrollpane-results (JScrollPane.
                         textarea-results
                         JScrollPane/VERTICAL_SCROLLBAR_ALWAYS
                         JScrollPane/HORIZONTAL_SCROLLBAR_NEVER))

(def lbl-history-area (JLabel. "Session History"))
(def textarea-history (doto (JTextArea. 400 400)
                        (.setLineWrap true)
                        (.setEditable false)))
(def scrollpane-history (JScrollPane.
                         textarea-history
                         JScrollPane/VERTICAL_SCROLLBAR_ALWAYS
                         JScrollPane/HORIZONTAL_SCROLLBAR_NEVER))

;; When code is run, send it off to robot-remote-server and save the code in the session history
(def btn-run (doto (JButton. "Run Keyword(s)")
               (.setForeground Color/RED)))
(def btn-clear-code (JButton. "Clear"))
(def btn-clear-results (JButton. "Clear"))
(def btn-clipboard-code (JButton. "Copy to Clipboard"))
(def btn-clipboard-results (JButton. "Copy to Clipboard"))
(def btn-history (JButton. "Session History"))
(def btn-refresh-history (JButton. "Refresh"))
;; ## Actions

;; Clear contents of code textarea
(with-action btn-clear-code evt
  (.setText textarea-code ""))

;; Clear contents of results textarea
(with-action btn-clear-results evt
  (.setText textarea-results ""))

;; Get code onto system clipboard
(with-action btn-clipboard-code evt
  (let [code-str (StringSelection. (.getText textarea-code))
        clip (.getSystemClipboard (Toolkit/getDefaultToolkit))]
    (.setContents clip code-str code-str)))

;; Get results onto system clipboard
(with-action btn-clipboard-results evt
  (let [results-str (StringSelection. (.getText textarea-results))
        clip (.getSystemClipboard (Toolkit/getDefaultToolkit))]
    (.setContents clip results-str results-str)))

;; Run keyword code
(with-action btn-run evt
  (when-not (empty? (.getText textarea-code))
    (let [raw-code (.getText textarea-code)
          lines (str/split-lines raw-code)
          code-exprs (parse-rf lines)
          output (with-out-str (doseq [[the-fn the-args] code-exprs]
                                 (let [raw-output (str (call "http://localhost:8270/RPC2" :run_keyword the-fn the-args))
                                       trimmed-output (str/trim raw-output)]
                                   (println (str (fn-name-to-rf the-fn) "=> " trimmed-output"\n")))))]
      (save-to-history raw-code)
      (.append textarea-results (str output "********************************************************************************\n")))))

;; Show history
(with-action btn-history evt
  (let [popup-layout (MigLayout. "wrap 1", "[][]")
        popup-pnl (doto (JPanel. popup-layout)
              (.add lbl-history-area)
              (.add scrollpane-history)
              (.add btn-refresh-history))
        popup-frame (doto (JFrame. "Robot Framework - Keyword Evaluator")
                      (.setSize 410 500)
                      (.setContentPane popup-pnl))]
    (javax.swing.SwingUtilities/invokeLater (do
                                              (.setVisible popup-frame true)
                                              (.setText textarea-history (output-history))))))

(with-action btn-refresh-history evt
  (.setText textarea-history (output-history)))

;; ## Layout
(def layout (MigLayout. "wrap 5", "[][]"))
(def pnl-main (doto (JPanel. layout)
                (.add lbl-code-area "span 3") ;; first row
                (.add lbl-results-area)
                (.add btn-history "top, right")
                (.add scrollpane-code "span 3") ;; second row
                (.add scrollpane-results "wrap, span 2")
                (.add btn-run) ;; third row
                (.add btn-clipboard-code)
                (.add btn-clear-code)
                (.add btn-clipboard-results)
                (.add btn-clear-results)))

(def frame (doto (JFrame. "Robot Framework - Keyword Evaluator")
             (.setSize 960 500)
             (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
             (.setContentPane pnl-main)))

(defn- create-and-show-gui []
  (.setVisible frame true))

(defn -main
  []
  (javax.swing.SwingUtilities/invokeLater create-and-show-gui))