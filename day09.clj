(def input (map (fn [x] (read-string (str \[ x \]))) (clojure.string/split-lines (slurp "input09"))))

(defn calc-diffs [series]
  (loop [all [series]]
    (let [recent (last all)]
      (if (every? zero? recent)
        all
        (recur (conj all (map (comp (partial apply -) reverse) (partition 2 1 (last all)))))))))

(defn extrapolate [all-diffs]
  (reduce + (map last all-diffs)))

(reduce + (map extrapolate (map calc-diffs input)))

;; Part 2

(defn extrapolate-front [all-diffs]
  (reduce #(- %2 %1) (reverse (map first all-diffs))))

(reduce + (map extrapolate-front (map calc-diffs input)))
