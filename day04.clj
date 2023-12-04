(require 'clojure.string)

(def lines (clojure.string/split-lines (slurp "input04")))

(defn parse-line [line]
  (let [[[_ nr winning nums]] (re-seq #"Card.*(\d+):(.*)\|(.*)" line)]
    {:nr (read-string nr)
     :winning (read-string (str \[ winning \]))
     :nums (read-string (str \[ nums \])) }))

(def input-data (map parse-line lines))

(require 'clojure.set)
(def won-numbers (map (fn [m] (clojure.set/intersection (set (:winning m)) (set (:nums m))) ) input-data))
(defn scoring [c]
  (if (empty? c)
    0
    (apply * (repeat (dec (count c)) 2))))

(reduce + (map scoring won-numbers))

;; Part 2

(def amount-wins (map (fn [m] {:id (:nr m), :amount 1, :wins (count (clojure.set/intersection (set (:winning m)) (set (:nums m))))} ) input-data))

;; this is a bit ugly and could use some destructuring of (first data)
;; however, I already used the symbol amount
;; meh
(loop [amount 0
       data amount-wins]
  (if (empty? data)
    amount
    (recur (+ amount (:amount (first data)))
           (let [below (rest data)
                 [to-duplicate tail] (split-at (:wins (first data)) below)]
             (concat (map (fn [m] (update m :amount + (:amount (first data)))) to-duplicate) tail)))))

