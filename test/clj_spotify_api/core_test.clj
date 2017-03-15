(ns clj-spotify-api.core-test
  (:require [clojure.test :refer :all]
            [clj-spotify-api :refer :all]))

(deftest get-an-album-test
  (is (= (get-an-album {:id "xyz"})
         {:method :get, :url "https://api.spotify.com/v1/albums/xyz"}))
  (is (= (get-an-album {:id "xyz" :market "NO"})
         {:method :get, :url "https://api.spotify.com/v1/albums/xyz&market=NO"})))

(deftest get-several-albums-test
  (is (= (get-several-albums {:ids ["foo" "bar" "baz"]})
         {:method :get, :url "https://api.spotify.com/v1/albums/?ids=foo,bar,baz"}))
  (is (= (get-several-albums {:ids (map identity ["foo" "bar" "baz"])})
         {:method :get, :url "https://api.spotify.com/v1/albums/?ids=foo,bar,baz"}))
  (is (= (get-several-albums {:ids ["foo" "bar" "baz"] :market "US"})
         {:method :get, :url "https://api.spotify.com/v1/albums/?ids=foo,bar,baz&market=US"}))
  (is (= (get-several-albums {:ids ["foo" "bar" "baz"] :market nil})
         {:method :get, :url "https://api.spotify.com/v1/albums/?ids=foo,bar,baz"}))
  (is (= (get-several-albums {:ids ["foo" "bar" "baz"] :unknown-key "hello"})
         {:method :get, :url "https://api.spotify.com/v1/albums/?ids=foo,bar,baz"})))

(deftest get-an-albums-tracks-test
  (is (= (get-an-albums-tracks {:id "foo"})
         {:method :get, :url "https://api.spotify.com/v1/albums/foo/tracks"}))
  (is (= (get-an-albums-tracks {:id "foo" :limit 20})
         {:method :get, :url "https://api.spotify.com/v1/albums/foo/tracks&limit=20"}))
  (is (= (get-an-albums-tracks {:id "foo" :market "UK"})
         {:method :get, :url "https://api.spotify.com/v1/albums/foo/tracks&market=UK"}))
  (or (= (get-an-albums-tracks {:id "foo" :limit 10 :offset "20"})
         {:method :get,
          :url "https://api.spotify.com/v1/albums/foo/tracks&limit10=&offset=20"})
      (= (get-an-albums-tracks {:id "foo" :limit 10 :offset "20"})
         {:method :get,
          :url "https://api.spotify.com/v1/albums/foo/tracks&offset=20&limit=10"})))

(deftest get-an-artist-test
  (is (= (get-an-artist {:id "foo" :market "US"}))
      {:method :get,
       :url "https://api.spotify.com/v1/artists/foo"}))

(deftest get-several-artists-test
  (is (= (get-several-artists {:ids ["foo","bar","baz"]}))
      {:method :get,
       :url "https://api.spotify.com/v1/artists?ids=foo,bar,baz"}))

(deftest get-an-artists-albums-test
  (is (= (get-an-artists-albums {:id "foo" :album_type [:album,"single"]}))
      {:method :get,
       :url "https://api.spotify.com/v1/artists/foo&album_type=album,single"}))

(deftest get-an-artists-top-tracks-test
  (is (= (get-an-artists-top-tracks {:id "foo" :country "SE"}))
      {:method :get
       :url "https://api.spotify.com/v1/artists/foo/top-tracks?country=SE"}))

(deftest get-an-artists-related-artists-test
  (is (= (get-an-artists-related-artists {:id "foo"}))
      {:method :get
       :url "https://api.spotify.com/v1/artists/foo/related-artists"}))

(deftest get-audio-analysis-for-a-track-test
  (is (= (get-audio-analysis-for-a-track {:id "foo"}))
      {:method :get
       :authorization true
       :url "https://api.spotify.com/v1/audio-analysis/foo"}))
