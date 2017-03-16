# clj-spotify-api
Spotify api wrapper for clojure with no dependencies


The library aims to provide a series of functions that mirror the semantics of the [spotify api reference](https://developer.spotify.com/web-api/endpoint-reference/). These functions take a clojure map and return a clojure map.
The returned map is guaranteed to have two keys `:method` and `:url`. If the endpoint requires authentication, then a third key `:authentication` is included (with a value of `true`)

Currently only endpoints that do not require authentication have been implemented.

As There are no dependencies, it is up to you how to use. As an example.

```clojure
(ns superapp.core
  (:require [clj-spotify-api.api :as api]
            [cheshire.core :refer [parse-string]]
            [aleph.http :as http]))

(defn spotify
  "a simple function for making GET requests"
  [f m]
  (let [{m :method url :url} (f m)]
    (case m
      :get (-> @(http/get url {:throw-exceptions? false})
               :body
               byte-streams/to-string
               (parse-string keyword))
      nil)))
```
We can now use it like this:

```clojure
(spotify api/get-artist {:id "4FD5ipqAUiHAAwERSGlDuX"})
```
The search function takes a list of keywords. It will double escape strings that contain two or more words. The keywords `:not` and `:or` are expaded into `"NOT"` and `"OR"`.

```clojure
(spotify api/search-artists
         {:filters {:year 2001} :keywords ["dr" :not "dre"] :limit 5})
```
```clojure
(api/search-artists {:keywords ["lil" :or "kim" :not "lil wayne"]})
{:method :get, :url "https://api.spotify.com/v1/search?q=lil+OR+kim+NOT+%22lil+wayne%22+&&type=artist"}
```
