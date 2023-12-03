(require 'clojure.string)

(def input (clojure.string/split-lines (slurp "input03")))
(def pos->char (into {} (apply concat (map-indexed (fn [yidx line] (map-indexed (fn [xidx c] [{:x xidx, :y yidx} c]) line)) input))))
(def symbols (into {} (filter (comp (complement #{\0 \1 \2 \3 \4 \5 \6 \7 \8 \9 \.}) second) pos->char)))
(def is-digit? (set "0123456789"))


(defn expand-left [x y nsc]
  (let [c (get pos->char {:y y :x x})]
    (if (is-digit? c)
      (expand-left (dec x) y (cons c nsc))
      nsc)))

(defn expand-right [x y nsc]
  (let [c (get pos->char {:y y :x x})]
    (if (is-digit? c)
      (expand-right (inc x) y (conj (vec nsc) c))
      nsc)))

(defn expand-left' [x y nsc]
  (let [pos {:y y :x x}
        c (get pos->char pos)]
    (if (is-digit? c)
      (expand-left' (dec x) y (conj nsc pos))
      nsc)))

(defn expand-right' [x y nsc]
  (let [pos {:y y :x x}
        c (get pos->char pos)]
    (if (is-digit? c)
      (expand-right' (inc x) y (conj nsc pos))
      nsc)))

(require 'clojure.set)
(defn numposs-of-symbol [[{x :x, y :y} _]]
  (clojure.set/union 
        (expand-left' (dec x) y #{})
        (expand-right' (inc x) y #{})
        (expand-left' (dec x) (dec y) #{})
        (expand-right' (inc x) (dec y) #{})
        (expand-left' (dec x) (inc y) #{})
        (expand-right' (inc x) (inc y) #{})
        (if (is-digit? (get pos->char {:x x :y (inc y)})) #{{:x x :y (inc y)}} #{})
        (if (is-digit? (get pos->char {:x x :y (dec y)})) #{{:x x :y (dec y)}} #{})))

(def charposs (apply clojure.set/union (map (comp numposs-of-symbol) symbols)))
(defn group-consecutive [coords]
  (second (reduce (fn [[last-x acc] {:keys [x] :as m}]
                    (if (= x (inc last-x))
                      [x (conj (rest acc) (conj (first acc) m))]
                      [x (conj acc [m])]))
                  [-2 '([])] coords)))
(def linecoords (map (comp (partial sort-by :x) second) (group-by :y charposs)))
(defn get-numbers-for-linecoords [linecoords]
  (map read-string (remove empty? (map (fn [c] (apply str (map pos->char c))) (mapcat group-consecutive linecoords)))))
(reduce + (get-numbers-for-linecoords linecoords))

;; Part 2
(def gear-candidates (into {} (filter (fn [[_ x]] (= x \*)) symbols)))

(def charposs-per-gear-cand (map (comp numposs-of-symbol) gear-candidates))
(def linecoords-per-gear-cand (map (fn [charposs] (map (comp (partial sort-by :x) second) (group-by :y charposs))) charposs-per-gear-cand))
(reduce + (map (partial apply *) (filter #(= (count %) 2) (map get-numbers-for-linecoords linecoords-per-gear-cand))))

