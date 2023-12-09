(require 'clojure.string)
(def lines (clojure.string/split-lines (slurp "input08")))
(def pattern (cycle (first lines)))
(def parsed-tree (map #(rest (first (re-seq #"(...) = \((...), (...)\)" %))) (drop 2 lines)))
(def tree (into {} (map (fn [[node left right]] [node {:left left, :right right}]) parsed-tree)))

(loop [cur-node "AAA"
       n 0
       pattern pattern]
  (assert cur-node)
  (if (= cur-node "ZZZ")
    n
    (recur (get-in tree [cur-node ({\L :left, \R :right} (first pattern))]) (inc n) (rest pattern))))


;; Part 2

;; brute force
#_(loop [cur-nodes (filter #(= (last %) \A) (map first parsed-tree))
       n 0
       pattern pattern]
  (if (every? #{\Z} (map last cur-nodes))
    n
    (recur (doall (map #(get-in tree [% ({\L :left, \R :right} (first pattern))]) cur-nodes))
           (inc n)
           (rest pattern))))

;; check all paths individually, use LCM
(def nums (for [x (filter #(= (last %) \A) (map first parsed-tree))]
            (loop [cur-node x
                   n 0
                   pattern pattern]
              (assert cur-node)
              (if (= (last cur-node) \Z)
                n
                (recur (get-in tree [cur-node ({\L :left, \R :right} (first pattern))]) (inc n) (rest pattern))))))

;; NOTE: I checked it gives the correct answer; there does not seem to be a condition that this actually works
;; it seems like the ..Z nodes have the same successors as the ..A nodes (but maybe in a different order)
;; TODO: implement LCM
