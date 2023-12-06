(require 'clojure.string)

(def races (apply map (fn [rtime record] {:racetime rtime :record record})
                  (map (fn [s] (read-string (str \[ (apply str s) \]))) (map (partial drop 9) (clojure.string/split-lines (slurp "input06"))))))
  

(defn calc-distance [max-time acc-time]
  (* acc-time (- max-time acc-time)))

(apply * (map count (map (fn [{:keys [racetime record]}] (filter #(< record %) (map (fn [acc-time] (calc-distance racetime acc-time)) (range racetime)))) races)))

;; Part 2


(let [[racetime record] (map (fn [s] (read-string (apply str (remove #{\space} s)))) (map (partial drop 9) (clojure.string/split-lines (slurp "input06"))))]
  (def racetime racetime)
  (def record record))

;; good enough
(count (filter #(< record %) (map (fn [acc-time] (calc-distance racetime acc-time)) (range racetime))))

