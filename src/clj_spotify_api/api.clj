(ns clj-spotify-api.api
  (:require [clj-spotify-api.utils :as u]
            [clojure.string :as str]))

(def ^:private ^:const base-url "https://api.spotify.com")

(defn- format-path [& parts]
  (str base-url (str/join "/" parts)))

(defn- add-params [path params]
  (let [p (u/format-params params)]
    (str path p)))

(defn- getm [url] {:method :get :url url})
(defn- putm [url] {:method :put :url url})

(defn- search
  "`https://developer.spotify.com/web-api/search-item/`"
  [m]
  (let [data (u/scrub-hash-map m)
        keywords (u/format-search-query (:keywords data))
        filters (u/format-search-filters (:filters data))
        params (u/format-params (select-keys data [:type :limit :market :offset]))
        base (str base-url "/v1/search?q=")]
    (getm (str base keywords "+" filters "&" params))))

(defn get-an-album
  "`https://developer.spotify.com/web-api/get-album/`"
  [{id :id :as m}]
  (getm (add-params (format-path "/v1/albums" id) (select-keys m [:market]))))

(defn get-several-albums
  "`https://developer.spotify.com/web-api/get-several-albums/`"
  [{ids :ids :as m}]
  (getm (add-params (format-path (str "/v1/albums/?ids=" (u/comma-separate-values ids)))
                    (select-keys m [:market]))))

(defn get-an-albums-tracks
  "`https://developer.spotify.com/web-api/get-albums-tracks/`"
  [{id :id :as m}]
  (getm (add-params (format-path "/v1/albums" id "tracks")
                    (select-keys m [:market :limit :offset]))))

(defn get-an-artist
  "`https://developer.spotify.com/web-api/get-artist/`"
  [{id :id}]
  (getm (format-path "/v1/artists" id)))

(defn get-several-artists
  "`https://developer.spotify.com/web-api/get-several-artists/`"
  [{ids :ids}]
  (getm (format-path (str "/v1/artists/?ids=" (u/comma-separate-values ids)))))

(defn get-an-artists-albums
  "`https://developer.spotify.com/web-api/get-artists-albums/`"
  [{id :id :as m}]
  (getm (add-params (format-path "/v1/artists/" id "/albums")
                    (select-keys m [:market :album_type :limit :offset]))))

(defn get-an-artists-top-tracks
  "`https://developer.spotify.com/web-api/get-artists-top-tracks/`"
  [{id :id :as m}]
  (getm (add-params (format-path "/v1/artists/" id "/top-tracks")
                    (select-keys m [:country]))))

(defn get-an-artists-related-artists
  "`https://developer.spotify.com/web-api/get-related-artists/`"
  [{id :id}]
  (getm (format-path "/v1/artists" id "related-artists")))

(defn get-audio-analysis-for-a-track
  "`https://developer.spotify.com/web-api/get-audio-analysis/`"
  [{id :id}]
  (assoc (getm (format-path "/v1/audio-analysis" id)) :authorization true))

(defn search-for-an-artist
  "`https://developer.spotify.com/web-api/search-item/`"
  [m]
  (search (assoc m :type "artist")))

(defn search-for-a-playlist
  "`https://developer.spotify.com/web-api/search-item/`"
  [m]
  (search (assoc m :type "playlist")))

(defn search-for-a-track
  "`https://developer.spotify.com/web-api/search-item/`"
  [m]
  (search (assoc m :type "track")))

(defn search-for-an-album
  "`https://developer.spotify.com/web-api/search-item/`"
  [m]
  (search (assoc m :type "album")))

(defn get-a-category
  "`https://developer.spotify.com/web-api/get-category/`"
  [{id :id :as m}]
  (assoc (getm (add-params (format-path "/v1/browse/categories" id)
                           (select-keys m [:country :locale]))) :authorization true))

(defn get-a-categorys-playlists [m] nil)
(defn get-a-list-of-a-users-playlists [m] nil)
(defn get-a-list-of-categories [m] nil)
(defn get-a-list-of-featured-playlists [m] nil)
(defn get-a-list-of-new-releases [m] nil)
(defn get-a-list-of-the-current-users-playlists [m] nil)
(defn get-a-playlist [m] nil)
(defn get-a-playlists-tracks [m] nil)
(defn get-a-users-profile [m] nil)
(defn get-a-users-top-artists-or-tracks [m] nil)
(defn get-audio-features-for-a-track [m] nil)
(defn get-audio-features-for-several-tracks [m] nil)
(defn get-current-users-profile [m] nil)
(defn get-current-user’s-recently-played-tracks [m] nil)
(defn get-followed-artists [m] nil)
(defn get-recommendations-based-on-seeds [m] nil)
(defn get-several-tracks [m] nil)
(defn get-users-saved-albums [m] nil)
(defn get-users-saved-tracks [m] nil)
(defn add-tracks-to-a-playlist [m] nil)
(defn change-a-playlists-details [m] nil)
(defn check-if-user-follows-users-or-artists [m] nil)
(defn check-if-users-follow-a-playlist [m] nil)
(defn check-users-saved-albums [m] nil)
(defn check-users-saved-tracks [m] nil)
(defn create-a-playlist [m] nil)
(defn follow-a-playlist [m] nil)
(defn follow-artists-or-users [m] nil)
(defn remove-tracks-from-a-playlist [m] nil)
(defn remove-users-saved-albums [m] nil)
(defn remove-users-saved-tracks [m] nil)
(defn reorder-a-playlists-tracks [m] nil)
(defn replace-a-playlists-tracks [m] nil)
(defn save-albums-for-user [m] nil)
(defn save-tracks-for-user [m] nil)
(defn unfollow-a-playlist [m] nil)
(defn unfollow-artists-or-users [m] nil)
