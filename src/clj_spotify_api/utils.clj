(ns clj-spotify-api.utils
  (:require [clojure.string :as str]))

(defn value-to-string [v]
  (when v
    (str/trim (str (if (keyword? v) (name v) v)))))

(defn scrub-hash-map
  "remove any keys that have `nil` as a value
  remove any keys that have empty `hash-maps`
  after calling scrub-hash-map recursivly on them
  f is called after scrubbing"
  ([m] (scrub-hash-map m identity))
  ([m f]
   (into {} (for [[k v] m]
              (cond (= v nil) nil
                    (and (map? v) (empty? v)) nil
                    (map? v) (let [v' (scrub-hash-map v f)]
                               (if (empty? v') nil [k v']))
                    (= v nil) nil
                    :else [k (f v)])))))

(defn format-search-term [t]
  (if-not (string? t)
    (str/upper-case (value-to-string t))
    (let [tr (str/trim t)]
      (if (str/includes? tr " ")
        (str "\"" tr "\"")
        tr))))

(defn comma-separate-values [ids]
  (if (nil? ids)
    nil
    (if (string? ids)
      (value-to-string ids)
      (str/join "," (map value-to-string ids)))))

(defn format-search-query [terms]
  (when terms
    (java.net.URLEncoder/encode (str/join " " (map format-search-term terms)))))

(defn q [& terms] terms)

(defn format-flat-map
  ;; need to add function on v (double quote or some shit")
  [m sep-str kv-str]
  (let [params (scrub-hash-map m #(-> % value-to-string format-search-term))]
    (when (and params (not= params {}))
      (str sep-str (str/join sep-str (for [[k v] params]
                                       (if (coll? v)
                                         (str (name k) kv-str (java.net.URLEncoder/encode (comma-separate-values v)))
                                         (str (name k) kv-str (java.net.URLEncoder/encode v)))))))))

(def format-params #(format-flat-map % "&" "="))

(def format-search-filters #(format-flat-map % "+" ":"))

(defn format-album-params [m]
  (when (and m (not= m {}))
    (let [params (scrub-hash-map (assoc (dissoc m :type) :album_type (:type m))
                                 #(-> % comma-separate-values))]
      (str/join "&" (for [[k v] params]
                      (str (name k) "=" (java.net.URLEncoder/encode v)))))))
