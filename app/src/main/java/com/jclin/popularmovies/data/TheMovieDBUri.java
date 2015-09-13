package com.jclin.popularmovies.data;

import android.net.Uri;

import com.jclin.popularmovies.BuildConfig;

public final class TheMovieDBUri
{
    private TheMovieDBUri()
    {
    }

    public static Uri buildForMovies(SortOrder sortOrder)
    {
        return new Uri.Builder()
            .scheme("http")
            .authority("api.themoviedb.org")
            .appendPath("3")
            .appendPath("discover")
            .appendPath("movie")
            .appendQueryParameter("sort_by", sortOrder.getQueryParam())
            .appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY)
            .build();
    }

    public static Uri buildForImage(String relativePath)
    {
        final String imageSizePath = "w185";

        return new Uri.Builder()
            .scheme("http")
            .authority("image.tmdb.org")
            .appendPath("t")
            .appendPath("p")
            .appendPath(imageSizePath)
            .appendPath(relativePath)
            .build();
    }
}
