package com.jclin.popularmovies.loaders;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.jclin.popularmovies.contentProviders.MoviesContract;
import com.jclin.popularmovies.contentProviders.TrailersContract;

import java.security.InvalidParameterException;

public final class LoaderFactory
{
    private LoaderFactory()
    {
    }

    public static Loader<Cursor> createFor(LoaderIDs loaderID, Context context, Bundle args)
    {
        switch (loaderID)
        {
            case PopularMovies:
                return new CursorLoader(
                    context,
                    MoviesContract.PopularMovies.ContentUri,
                    MoviesContract.PopularMovies.Columns.Projection,
                    null,
                    null,
                    null
                    );

            case HighlyRatedMovies:
                return new CursorLoader(
                    context,
                    MoviesContract.HighlyRatedMovies.ContentUri,
                    MoviesContract.HighlyRatedMovies.Columns.Projection,
                    null,
                    null,
                    null
                    );

            case FavoriteMovies:
                return new CursorLoader(
                    context,
                    MoviesContract.FavoriteMovies.ContentUri,
                    MoviesContract.FavoriteMovies.Columns.Projection,
                    null,
                    null,
                    null
                    );

            case MovieTrailers:
                return new CursorLoader(
                    context,
                    TrailersContract.contentUriFor(args.getLong(TrailersContract.MOVIE_ID_BUNDLE_KEY)),
                    TrailersContract.Columns.Projection,
                    null,
                    null,
                    null
                    );
        }

        throw new InvalidParameterException("Unknown loaderID = " + loaderID);
    }
}
