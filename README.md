# Popular Movies

Popular Movies is an Android app that is built as a part of [Project 01 and Project 02](https://docs.google.com/document/d/1ZlN1fUsCSKuInLECcJkslIqvpKlP7jWL2TP9m6UiA6I/pub?embedded=true#h.mj25h5o650wp) of the Android Developer Nanodegree course at [Udacity](http://www.udacity.com).

## Development Environment

Popular Movies is currently being developed with Android Studio 1.3.2, and Java SE JDK 8. It currently targets API LEVEL 22 (Lollipop), with API LEVEL 15 (Ice Cream Sandwich) as the minimum for compatible devices.

## API Keys

An API key for [The Movie DB](https://www.themoviedb.org/) is required in order to compile and run Popular Movies. Once you have an API key, you must create a `dev_api_keys.properties` file under `<repo_path>/app` and assign the key to the `theMovieDbApiKey` property.

For example, if `<repo_path>` was `/PopularMovies`, then `/PopularMovies/app/dev_api_keys.properties` would contain something similar to:

```gradle
theMovieDbApiKey = "aaabbbcccddd"
```

The project will fail to build if either the file or property is missing as this API key is assigned to the `BuildConfig.THE_MOVIE_DB_API_KEY` constant during compilation.