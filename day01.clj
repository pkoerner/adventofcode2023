(require 'clojure.string)
(def input (clojure.string/split-lines (slurp "input01")))
(def digits (map (fn [s] (keep (set (map char "0123456789")) s)) input))
(defn combine-first-last [c] (read-string (str (first c) (last c))))
(def calibration-values (map combine-first-last digits))
(reduce + calibration-values)

;; Part 2

;; screw it, time to unleash the regex

(re-seq #"one|two|three|four|five|six|seven|eight|nine|\d" )

(def magic-regex #"(?=(one|two|three|four|five|six|seven|eight|nine|\d))")
(def digits2 (map (fn [s] (map second (re-seq magic-regex s))) input))
(def replacement-map (merge (zipmap (map str "0123456789") (range 10))
                            (zipmap ["one" "two" "three" "four" "five" "six" "seven" "eight" "nine"] (rest (range 10)))))
(reduce + (map (fn [nums] (combine-first-last (map replacement-map nums))) digits2))
 
