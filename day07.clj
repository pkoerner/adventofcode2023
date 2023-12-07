(require 'clojure.string)

(def data (map #(clojure.string/split % #" ") (clojure.string/split-lines (slurp "input07"))))

(defn hand-type [s]
  (sort-by - (remove #{1} (vals (frequencies s)))))

(hand-type "AAA33")

(def type-score {[5] 7, [4] 6, [3 2] 5, [3] 4, [2 2] 3, [2] 2, [] 1})
(def score {\A 14, \K 13, \Q 12, \J 11, \T 10, \9 9, \8 8, \7 7, \6 6, \5 5, \4 4, \3 3, \2 2})


(defn worse-hand? [hand1 hand2]
  (let [score1 (type-score (hand-type hand1))
        score2 (type-score (hand-type hand2))]
    (cond (< score1 score2) true
          (> score1 score2) false
          :otherwise (loop [hand1 hand1, hand2 hand2]
                       (let [score1 (score (first hand1))
                             score2 (score (first hand2))]
                         (cond (< score1 score2) true
                               (> score1 score2) false
                               :otherwise (recur (rest hand1) (rest hand2))))))))

(def ordered (sort (comp (fn [a b] (worse-hand? (first a) (first b)))) data))
(reduce + (map (partial apply *) (map vector (map (comp read-string second) ordered) (rest (range)))))

;; Part 2

(def score {\A 14, \K 13, \Q 12, \J 1, \T 10, \9 9, \8 8, \7 7, \6 6, \5 5, \4 4, \3 3, \2 2})

(defn hand-type [s]
  (let [type-without-jokers (vec (sort-by - (vals (frequencies (remove #{\J} s)))))
        type-with-jokers (update type-without-jokers 0 (fnil + 0) (count (filter #{\J} s)))]
    (remove #{1} type-with-jokers)))

(def ordered (sort (comp (fn [a b] (worse-hand? (first a) (first b)))) data))
(reduce + (map (partial apply *) (map vector (map (comp read-string second) ordered) (rest (range)))))
