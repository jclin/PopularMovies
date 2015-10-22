package com.jclin.popularmovies.data;

import android.net.Uri;

import com.jclin.popularmovies.BuildConfig;

public final class UriBuilder
{
    private static final String Protocol            = "http";
    private static final String TheMovieDbAuthority = "api.themoviedb.org";

    private UriBuilder()
    {
    }

    public static Uri buildForMovies(SortOrder sortOrder)
    {
        return new Uri.Builder()
            .scheme(Protocol)
            .authority(TheMovieDbAuthority)
            .appendPath("3")
            .appendPath("discover")
            .appendPath("movie")
            .appendQueryParameter("sort_by", sortOrder.getQueryParam())
            .appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY)
            .build();
    }

    public static Uri buildForTrailers(long movieId)
    {
        return new Uri.Builder()
            .scheme(Protocol)
            .authority(TheMovieDbAuthority)
            .appendPath("3")
            .appendPath("movie")
            .appendPath(String.valueOf(movieId))
            .appendPath("videos")
            .appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY)
            .build();
    }

    public static Uri buildForYouTubeVideo(String videoKey)
    {
        return new Uri.Builder()
            .scheme(Protocol)
            .authority("www.youtube.com")
            .appendPath("v")
            .appendPath(videoKey)
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
