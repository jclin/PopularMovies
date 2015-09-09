package com.jclin.popularmovies.data;

import android.net.Uri;

public final class TheMovieDBImageUri
{
    private TheMovieDBImageUri()
    {
    }

    public static Uri buildFor(String relativePath)
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
