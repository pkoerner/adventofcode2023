(def lines (clojure.string/split-lines (slurp "input02")))
(defn extract-shown [line] 
  (let [[gamestr shownstr] (clojure.string/split line #":")
        games (map (fn [x] (clojure.string/split (clojure.string/trim x) #",")) (clojure.string/split shownstr #";"))
        game-tuples (map (fn [game] (map (fn [s] (map clojure.string/trim (clojure.string/split (clojure.string/trim s) #" "))) game)) games)
        game-maps (map (fn [game-tuple] (into {} (map (comp vec reverse) game-tuple))) game-tuples)
        ]
    {:game (Long/parseLong (apply str (drop 5 gamestr)))
     :showns game-maps
     }))

(map extract-shown lines)
(def data (->> lines
               (map extract-shown)
               (map (fn [m] (update m :showns (fn [c] (map (fn [m] (into {} (map (fn [[k v]] [k (Long/parseLong v)]) m))) c)))))
               ))
(def max-seen (map (fn [game] (update game :showns (fn [c] (apply merge-with max c)))) data))
(reduce + (map :game (filter (fn [game] (let [m (:showns game)] 
                                          (and (<= (get m "red" 0) 12) (<= (get m "green" 0) 13) (<= (get m "blue" 0) 14))))
                             max-seen)))

;; Part 2

(reduce + (map (comp (partial apply *) vals :showns) max-seen))

