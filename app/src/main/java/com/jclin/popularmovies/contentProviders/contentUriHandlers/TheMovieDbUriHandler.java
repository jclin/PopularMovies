package com.jclin.popularmovies.contentProviders.contentUriHandlers;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import com.jclin.popularmovies.App;
import com.jclin.popularmovies.contentProviders.MoviesContract;
import com.jclin.popularmovies.contentProviders.UriSwitches;
import com.jclin.popularmovies.data.Movie;
import com.jclin.popularmovies.data.SortOrder;
import com.jclin.popularmovies.data.TheMovieDbApi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

public abstract class TheMovieDbUriHandler implements IContentUriHandler
{
    private static final String LOG_TAG = TheMovieDbUriHandler.class.getName();

    private final SortOrder _sortOrder;
    private Hashtable<Long, Movie> _retrievedMovies;

    protected TheMovieDbUriHandler(SortOrder sortOrder)
    {
        _sortOrder       = sortOrder;
        _retrievedMovies = new Hashtable<>(0);
    }

    @Override
    public final Cursor query(
        UriSwitches uriSwitch,
        Uri rawUri,
        String[] projection,
        String selection,
        String[] selectionArgs,
        String sortOrder
        )
    {
        verify(uriSwitch);

        if (uriSwitch == allMoviesUriSwitch())
        {
            return tryFetchAllMovies(rawUri);
        }

        return tryFetchMovie(ContentUris.parseId(rawUri), rawUri);
    }

    @Override
    public final Uri insert(
        UriSwitches uriSwitch,
        Uri rawUri,
        ContentValues values
        )
    {
        Log.i(LOG_TAG, "Inserting does not make sense for this instance. Skipping...");
        return null;
    }

    @Override
    public final int update(
        UriSwitches uriSwitch,
        Uri rawUri,
        ContentValues values,
        String selection,
        String[] selectionArgs
        )
    {
        Log.i(LOG_TAG, "Updating does not make sense for this instance. Skipping...");
        return 0;
    }

    @Override
    public final int delete(
        UriSwitches uriSwitch,
        Uri rawUri,
        String selection,
        String[] selectionArgs
        )
    {
        Log.i(LOG_TAG, "Deleting does not make sense for this instance. Skipping...");
        return 0;
    }

    protected abstract UriSwitches allMoviesUriSwitch();
    protected abstract UriSwitches singleMovieUriSwitch();

    private void verify(UriSwitches uriSwitch)
    {
        if (uriSwitch != allMoviesUriSwitch() && uriSwitch != singleMovieUriSwitch())
        {
            throw new UnsupportedOperationException("Unsupported uri switch = " + uriSwitch);
        }
    }

    private Cursor tryFetchMovie(long movieId, Uri uri)
    {
        if (!_retrievedMovies.isEmpty())
        {
            Log.i(LOG_TAG, "tryFetchMovie returning cursor for cached movie id = " + movieId);
            return buildCursor(_retrievedMovies.get(movieId), uri);
        }

        Log.i(LOG_TAG, "Fetching all movies before retrieving movie with id = " + movieId);
        fetchAllMovies();

        return buildCursor(_retrievedMovies.get(movieId), uri);
    }

    private Cursor tryFetchAllMovies(Uri uri)
    {
        if (!_retrievedMovies.isEmpty())
        {
            Log.i(LOG_TAG, "tryFetchAllMovies returning cursor for cached movies...");
            return buildCursor(_retrievedMovies.values(), uri);
        }

        fetchAllMovies();

        return buildCursor(_retrievedMovies.values(), uri);
    }

    private void fetchAllMovies()
    {
        Log.i(LOG_TAG, "Fetching movies from TheMovieDB sorted by " + _sortOrder);
        _retrievedMovies = TheMovieDbApi.fetchMovies(_sortOrder);
    }

    private static Cursor buildCursor(Collection<Movie> retrievedMovies, Uri uri)
    {
        MatrixCursor cursor = new MatrixCursor(
            MoviesContract.PopularMovies.Columns.Projection,
            retrievedMovies.size()
            );

        for (Movie movie : retrievedMovies)
        {
            cursor.addRow(new Object[]
            {
                movie.getId(),
                movie.getOriginalTitle(),
                movie.getOverview(),
                movie.getPosterPath(),
                movie.getVoteAverage(),
                movie.getReleaseDateMilliseconds()
            });
        }

        cursor.setNotificationUri(
            App.getContext().getContentResolver(),
            uri
            );

        return cursor;
    }

    private static Cursor buildCursor(Movie movie, Uri uri)
    {
        ArrayList<Movie> movieCollection = new ArrayList<>(1);
        movieCollection.add(movie);

        return buildCursor(movieCollection, uri);
    }
}
