package com.jclin.popularmovies.contentProviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import com.jclin.popularmovies.data.TheMovieDbApi;
import com.jclin.popularmovies.data.Trailer;

import java.util.Hashtable;

public final class TrailersContentProvider extends ContentProvider
{
    private static final String LOG_TAG = TrailersContentProvider.class.getName();

    private static final UriMatcher s_uriMatcher = buildUriMatcher();

    private Hashtable<Long, Trailer[]> _retrievedTrailers = new Hashtable<>();

    private static UriMatcher buildUriMatcher()
    {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(TrailersContract.Authority, TrailersContract.TableName + "/#", UriSwitches.Trailers.value());

        return uriMatcher;
    }

    @Override
    public boolean onCreate()
    {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        UriSwitches uriSwitch = UriSwitches.parse(s_uriMatcher.match(uri));
        if (uriSwitch != UriSwitches.Trailers)
        {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        final long movieId = Long.valueOf(uri.getLastPathSegment());

        Cursor cursor = buildCursorFor(fetchTrailersFor(movieId));

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri)
    {
        switch (UriSwitches.parse(s_uriMatcher.match(uri)))
        {
            case Trailers:
                return TrailersContract.Types.DirectoryType;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        Log.i(LOG_TAG, "Inserting is not supported. Skipping...");

        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        Log.i(LOG_TAG, "Deleting is not supported. Skipping...");

        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        Log.i(LOG_TAG, "Updating is not supported. Skipping...");

        return 0;
    }

    private Trailer[] fetchTrailersFor(long movieId)
    {
        if (_retrievedTrailers.containsKey(movieId))
        {
            Log.i(LOG_TAG, "Trailers already retrieved for movie with id '" + movieId + "'");
            return _retrievedTrailers.get(movieId);
        }

        Log.i(LOG_TAG, "Fetching trailers for movie with id '" + movieId + "' from The Movie DB...");
        _retrievedTrailers.put(movieId, TheMovieDbApi.fetchTrailersFor(movieId));

        return _retrievedTrailers.get(movieId);
    }

    private Cursor buildCursorFor(Trailer[] trailers)
    {
        MatrixCursor matrixCursor = new MatrixCursor(TrailersContract.Columns.Projection);
        for (Trailer trailer : trailers)
        {
            matrixCursor.addRow(
                new Object[]
                {
                    trailer.id(),
                    trailer.youTubeUri().toString(),
                    trailer.title()
                }
            );
        }

        return matrixCursor;
    }
}
