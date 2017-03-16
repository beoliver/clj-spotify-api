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


(defn get-artist
  "`https://developer.spotify.com/web-api/get-artist/`

  required keys:
  :id         - an artist id
  "
  [{id :id}]
  (getm (format-path "/v1/artists" id)))

(defn get-several-artists
  "`https://developer.spotify.com/web-api/get-several-artists/`

  required keys:
  :id         - a collection of artist ids
  "
  [{ids :ids}]
  (getm (format-path (str "/v1/artists/?ids=" (u/comma-separate-values ids)))))

(defn get-artists-albums
  "`https://developer.spotify.com/web-api/get-artists-albums/`

  required keys:
  :id         - an artist id

  optional keys:
  :market     - an ISO 3166-1 alpha-2 country code
  :album_type - one of [album, single, appears_on, compilation].
                String or Keyword
  :limit      - Integer or String
  :offset     - Integer or String
  "
  [{id :id :as m}]
  (getm (add-params (format-path "/v1/artists/" id "/albums")
                    (select-keys m [:market :album_type :limit :offset]))))

(defn get-artists-top-tracks
  "`https://developer.spotify.com/web-api/get-artists-top-tracks/`

  required keys:
  :id         - an artist id
  :country    - an ISO 3166-1 alpha-2 country code
  "
  [{id :id :as m}]
  (getm (add-params (format-path "/v1/artists/" id "/top-tracks")
                    (select-keys m [:country]))))

(defn get-related-artists
  "`https://developer.spotify.com/web-api/get-related-artists/`

  required keys:
  :id         - an artist id
  "
  [{id :id}]
  (getm (format-path "/v1/artists" id "related-artists")))

;; albums ----------------------------------------------------------------------

(defn get-album
  "`https://developer.spotify.com/web-api/get-album/`

  required keys:
  :id         - an album id

  optional keys:
  :market     - an ISO 3166-1 alpha-2 country code
  "
  [{id :id :as m}]
  (getm (add-params (format-path "/v1/albums" id) (select-keys m [:market]))))

(defn get-several-albums
  "`https://developer.spotify.com/web-api/get-several-albums/`

  required keys:
  :ids        - a collection of artist ids

  optional keys:
  :market     - an ISO 3166-1 alpha-2 country code
  "
  [{ids :ids :as m}]
  (getm (add-params (format-path (str "/v1/albums/?ids=" (u/comma-separate-values ids)))
                    (select-keys m [:market]))))

(defn get-albums-tracks
  "`https://developer.spotify.com/web-api/get-albums-tracks/`

  required keys:
  :id         - an album id

  optional keys:
  :market     - an ISO 3166-1 alpha-2 country code
  :limit      - Integer or String
  :offset     - Integer or String
  "
  [{id :id :as m}]
  (getm (add-params (format-path "/v1/albums" id "tracks")
                    (select-keys m [:market :limit :offset]))))

(defn get-track
  "`https://developer.spotify.com/web-api/get-track/`

  required keys:
  :id         - a track id

  optional keys:
  :market     - an ISO 3166-1 alpha-2 country code
  "
  [{id :id :as m}]
  (getm (add-params (format-path "/v1/tracks" id) (select-keys m [:market]))))

(defn get-several-tracks
  "`https://developer.spotify.com/web-api/get-several-tracks/`

  required keys:
  :ids        - a collection of track ids

  optional keys:
  :market     - an ISO 3166-1 alpha-2 country code
  "
  [{ids :ids :as m}]
  (getm (add-params (format-path (str "/v1/tracks/?ids=" (u/comma-separate-values ids)))
                    (select-keys m [:market]))))

(defn search-item
  "`https://developer.spotify.com/web-api/search-item/`

  required keys:
  :keywords   - a collection of strings Strings, the keywords :or and :not
                are converted to OR and NOT, see the web-api page
  :type       - one of :album, :artist, :playlist, :track (keyword or string)

  optional keys:
  :filters    - a map containing some or all of the keys:
               :album, :artist, :track, :year, :tag, :genre, :upc, :isrc
  :market     - an ISO 3166-1 alpha-2 country code
  :limit      - Integer or String
  :offset     - Integer or String
  "
  [m]
  (let [data (u/scrub-hash-map m)
        keywords (u/format-search-query (:keywords data))
        filters (u/format-search-filters (:filters data))
        params (u/format-params (select-keys data [:type :limit :market :offset]))
        base (str base-url "/v1/search?q=")]
    (getm (str base keywords "+" filters "&" params))))

(defn search-artists
  "`https://developer.spotify.com/web-api/search-item/`

  required keys:
  :keywords   - a collection of strings Strings, the keywords :or and :not
                are converted to OR and NOT, see the web-api page

  optional keys:
  :filters    - a map containing some or all of the keys:
               :album, :artist, :track, :year, :tag, :genre, :upc, :isrc
  :market     - an ISO 3166-1 alpha-2 country code
  :limit      - Integer or String
  :offset     - Integer or String
  "
  [m]
  (search-item (assoc m :type "artist")))

(defn search-playlists
  "`https://developer.spotify.com/web-api/search-item/`

  required keys:
  :keywords   - a collection of strings Strings, the keywords :or and :not
                are converted to OR and NOT, see the web-api page

  optional keys:
  :filters    - a map containing some or all of the keys:
               :album, :artist, :track, :year, :tag, :genre, :upc, :isrc
  :market     - an ISO 3166-1 alpha-2 country code
  :limit      - Integer or String
  :offset     - Integer or String
  "
  [m]
  (search-item (assoc m :type "playlist")))

(defn search-tracks
  "`https://developer.spotify.com/web-api/search-item/`

  required keys:
  :keywords   - a collection of strings Strings, the keywords :or and :not
                are converted to OR and NOT, see the web-api page

  optional keys:
  :filters    - a map containing some or all of the keys:
               :album, :artist, :track, :year, :tag, :genre, :upc, :isrc
  :market     - an ISO 3166-1 alpha-2 country code
  :limit      - Integer or String
  :offset     - Integer or String
  "
  [m]
  (search-item (assoc m :type "track")))

(defn search-albums
  "`https://developer.spotify.com/web-api/search-item/`

  required keys:
  :keywords   - a collection of strings Strings, the keywords :or and :not
                are converted to OR and NOT, see the web-api page

  optional keys:
  :filters    - a map containing some or all of the keys:
               :album, :artist, :track, :year, :tag, :genre, :upc, :isrc
  :market     - an ISO 3166-1 alpha-2 country code
  :limit      - Integer or String
  :offset     - Integer or String
  "
  [m]
  (search-item (assoc m :type "album")))

(defn get-audio-analysis
  "`https://developer.spotify.com/web-api/get-audio-analysis/`"
  [{id :id}]
  (assoc (getm (format-path "/v1/audio-analysis" id)) :authorization true))

(defn get-category
  "`https://developer.spotify.com/web-api/get-category/`"
  [{id :category_id :as m}]
  (assoc (getm (add-params (format-path "/v1/browse/categories" id)
                           (select-keys m [:country :locale]))) :authorization true))

(defn get-categorys-playlists
  "`https://developer.spotify.com/web-api/get-categorys-playlists/`"
  [{id :category_id :as m}]
  (assoc (getm (add-params (format-path "/v1/browse/categories" id "playlists")
                           (select-keys m [:country :limit :offset]))) :authorization true))

(defn get-list-users-playlists
  "`https://developer.spotify.com/web-api/get-list-users-playlists/`"
  [{id :user_id :as m}]
  (assoc (getm (add-params (format-path "/v1/users" id "playlists")
                           (select-keys m [:limit :offset]))) :authorization true))

(defn get-list-categories
  "`https://developer.spotify.com/web-api/get-list-categories/`"
  [m]
  (assoc (getm (add-params (format-path "/v1/browse/categories")
                           (select-keys m [:country :limit :offset :locale]))) :authorization true))

(defn get-list-featured-playlists
  "`https://developer.spotify.com/web-api/get-list-featured-playlists/`"
  [m]
  (assoc (getm (add-params (format-path "/v1/browse/featured-playlists")
                           (select-keys m [:country :limit :offset :locale]))) :authorization true))


(defn get-list-new-releases
  "`https://developer.spotify.com/web-api/get-list-new-releases/`"
  [m]
  (assoc (getm (add-params (format-path "/v1/browse/new-releases")
                           (select-keys m [:country :limit :offset]))) :authorization true))

(defn get-list-users-playlists
  "`https://developer.spotify.com/web-api/get-list-users-playlists/`"
  [m] nil)

(defn get-playlist [m]
  "`https://developer.spotify.com/web-api/get-playlist/`"
  nil)

(defn get-playlists-tracks
  "`https://developer.spotify.com/web-api/get-playlists-tracks/`"
  [m] nil)

(defn get-users-profile
  "`https://developer.spotify.com/web-api/get-users-profile/`"
  [m] nil)

(defn get-users-top-artists-and-tracks
  "`https://developer.spotify.com/web-api/get-users-top-artists-and-tracks/`"
  [m] nil)

(defn get-audio-features-for-a-track
  "`https://developer.spotify.com/web-api/get-audio-features/`"
  [m] nil)

(defn get-several-audio-features
  "`https://developer.spotify.com/web-api/get-several-audio-features/`"
  [m] nil)

(defn get-current-users-profile
  "`https://developer.spotify.com/web-api/get-current-users-profile/`"
  [m] nil)

(defn get-recently-played
  "`https://developer.spotify.com/web-api/web-api-personalization-endpoints/get-recently-played/`"
  [m] nil)

(defn get-followed-artists
  "`https://developer.spotify.com/web-api/get-followed-artists/`"
  [m] nil)

(defn get-recommendations
  "`https://developer.spotify.com/web-api/get-recommendations/`"
  [m] nil)

(defn get-users-saved-albums
  "`https://developer.spotify.com/web-api/get-users-saved-albums/`"
  [m] nil)

(defn get-users-saved-tracks
  "`https://developer.spotify.com/web-api/get-users-saved-tracks/`"
  [m] nil)

(defn add-tracks-to-playlist
  "`https://developer.spotify.com/web-api/add-tracks-to-playlist/`"
  [m] nil)

(defn change-playlist-details
  "`https://developer.spotify.com/web-api/change-playlist-details/`"
  [m] nil)

(defn check-current-user-follows
  "`https://developer.spotify.com/web-api/check-current-user-follows/`"
  [m] nil)

(defn check-user-following-playlist
  "`https://developer.spotify.com/web-api/check-user-following-playlist/`"
  [m] nil)

(defn check-users-saved-albums
  "`https://developer.spotify.com/web-api/check-users-saved-albums/`"
  [m] nil)

(defn check-users-saved-tracks
  "`https://developer.spotify.com/web-api/check-users-saved-tracks/`"
  [m] nil)

(defn create-playlist
  "`https://developer.spotify.com/web-api/create-playlist/`"
  [m] nil)

(defn follow-playlist
  "`https://developer.spotify.com/web-api/follow-playlist/`"
  [m] nil)

(defn follow-artists-users
  "`https://developer.spotify.com/web-api/follow-artists-users/`"
  [m]
  nil)

(defn remove-tracks-playlist
  "`https://developer.spotify.com/web-api/remove-tracks-playlist/`"
  [m] nil)

(defn remove-albums-user
  "`https://developer.spotify.com/web-api/remove-albums-user/`"
  [m] nil)

(defn remove-tracks-user
  "`https://developer.spotify.com/web-api/remove-tracks-user/`"
  [m] nil)

(defn reorder-playlists-tracks
  "`https://developer.spotify.com/web-api/reorder-playlists-tracks/`"
  [m] nil)

(defn replace-playlists-tracks
  "`https://developer.spotify.com/web-api/replace-playlists-tracks/`"
  [m] nil)

(defn save-albums-user
  "`https://developer.spotify.com/web-api/save-albums-user/`"
  [m] nil)

(defn save-tracks-user
  "`https://developer.spotify.com/web-api/save-tracks-user/`"
  [m] nil)

(defn unfollow-playlist
  "`https://developer.spotify.com/web-api/unfollow-playlist/`"
  [m] nil)

(defn unfollow-artists-users
  "`https://developer.spotify.com/web-api/unfollow-artists-users/`"
  [m] nil)
