package com.jclin.popularmovies.contentProviders.contentUriHandlers;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;

import com.jclin.popularmovies.App;
import com.jclin.popularmovies.contentProviders.MoviesContract;
import com.jclin.popularmovies.contentProviders.MoviesDbHelper;
import com.jclin.popularmovies.contentProviders.UriSwitches;

public final class FavoriteMoviesUriHandler implements IContentUriHandler
{
    private final Context _context;
    private final MoviesDbHelper _moviesDbHelper;

    public FavoriteMoviesUriHandler(Context context, MoviesDbHelper moviesDbHelper)
    {
        _context        = context;
        _moviesDbHelper = moviesDbHelper;
    }

    @Override
    public Cursor query(
        UriSwitches uriSwitch,
        Uri rawUri,
        String[] projection,
        String selection,
        String[] selectionArgs,
        String sortOrder
        )
    {
        Cursor cursor;
        switch (uriSwitch)
        {
            case FavoriteMovie:
                cursor = _moviesDbHelper.getReadableDatabase().query(
                    MoviesContract.FavoriteMovies.TableName,
                    new String[] { MoviesContract.FavoriteMovies.Columns._ID },
                    whereClauseForId(),
                    new String[] { String.valueOf(ContentUris.parseId(rawUri)) },
                    null,
                    null,
                    sortOrder
                    );

                break;

            case FavoriteMovies:
                cursor = _moviesDbHelper.getReadableDatabase().query(
                    MoviesContract.FavoriteMovies.TableName,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                    );

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + rawUri);
        }

        cursor.setNotificationUri(_context.getContentResolver(), rawUri);

        return cursor;
    }

    @Override
    public Uri insert(UriSwitches uriSwitch, Uri rawUri, ContentValues values)
    {
        verify(uriSwitch);

        final long movieId = ContentUris.parseId(rawUri);
        final SQLiteDatabase database = _moviesDbHelper.getWritableDatabase();

        if (movieEntryAlreadyExists(movieId))
        {
            update(
                UriSwitches.FavoriteMovie,
                rawUri,
                values,
                whereClauseForId(),
                new String[] { String.valueOf(movieId) }
                );

            return MoviesContract.FavoriteMovies.contentUriFor(movieId);
        }

        long insertedId = database.insert(MoviesContract.FavoriteMovies.TableName, null, values);
        if (insertedId != movieId)
        {
            throw new SQLiteException(String.format(
                    "Expected inserted movie to have an id of %d, but was %d",
                    movieId,
                    insertedId)
            );
        }

        final Uri returnUri = MoviesContract.FavoriteMovies.contentUriFor(insertedId);

        invokeChangeNotification(returnUri);

        return returnUri;
    }

    @Override
    public int update(
        UriSwitches uriSwitch,
        Uri rawUri,
        ContentValues values,
        String selection,
        String[] selectionArgs
        )
    {
        verify(uriSwitch);

        final long movieId = ContentUris.parseId(rawUri);
        final SQLiteDatabase database = _moviesDbHelper.getWritableDatabase();

        int numRowsUpdated = database.update(
            MoviesContract.FavoriteMovies.TableName,
            values,
            whereClauseForId(),
            new String[]{String.valueOf(movieId)}
            );

        if (numRowsUpdated > 0)
        {
            invokeChangeNotification(rawUri);
        }

        return numRowsUpdated;
    }

    @Override
    public int delete(
        UriSwitches uriSwitch,
        Uri rawUri,
        String selection,
        String[] selectionArgs
        )
    {
        int numRowsDeleted = 0;

        switch (uriSwitch)
        {
            case FavoriteMovie:
                numRowsDeleted = _moviesDbHelper.getWritableDatabase().delete(
                    MoviesContract.FavoriteMovies.TableName,
                    whereClauseForId(),
                    new String[] { String.valueOf(ContentUris.parseId(rawUri)) }
                    );

                break;

            case FavoriteMovies:
                numRowsDeleted = _moviesDbHelper.getWritableDatabase().delete(
                    MoviesContract.FavoriteMovies.TableName,
                    selection,
                    selectionArgs
                    );

                break;
        }

        if (numRowsDeleted > 0)
        {
            invokeChangeNotification(rawUri);
        }

        return numRowsDeleted;
    }

    private boolean movieEntryAlreadyExists(long movieId)
    {
        Cursor cursor = query(
            UriSwitches.FavoriteMovie,
            MoviesContract.FavoriteMovies.contentUriFor(movieId),
            new String[0],
            null,
            new String[0],
            null
            );

        boolean movieEntryExists = cursor.moveToFirst();

        cursor.close();

        return movieEntryExists;
    }

    private String whereClauseForId()
    {
        return MoviesContract.FavoriteMovies.Columns._ID + " = ?";
    }

    private void verify(UriSwitches uriSwitch)
    {
        if (uriSwitch != UriSwitches.FavoriteMovie)
        {
            throw new UnsupportedOperationException("Unknown uri switch: " + uriSwitch);
        }
    }

    private void invokeChangeNotification(Uri uri)
    {
        App.getContext().getContentResolver().notifyChange(uri, null);
    }
}
