package com.jclin.popularmovies.contentProviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.jclin.popularmovies.contentProviders.contentUriHandlers.ContentUriHandlerFactory;

public final class MoviesContentProvider extends ContentProvider
{
    private static final UriMatcher s_uriMatcher = buildUriMatcher();
    private ContentUriHandlerFactory _uriHandlerFactory;

    private static UriMatcher buildUriMatcher()
    {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        final String authority = MoviesContract.Authority;

        uriMatcher.addURI(authority, MoviesContract.FavoriteMovies.TableName, UriSwitches.FavoriteMovies.value());
        uriMatcher.addURI(authority, MoviesContract.FavoriteMovies.TableName + "/#", UriSwitches.FavoriteMovie.value());

        uriMatcher.addURI(authority, MoviesContract.PopularMovies.TableName, UriSwitches.PopularMovies.value());
        uriMatcher.addURI(authority, MoviesContract.PopularMovies.TableName + "/#", UriSwitches.PopularMovie.value());

        uriMatcher.addURI(authority, MoviesContract.HighlyRatedMovies.TableName, UriSwitches.HighlyRatedMovies.value());
        uriMatcher.addURI(authority, MoviesContract.HighlyRatedMovies.TableName + "/#", UriSwitches.HighlyRatedMovie.value());

        return uriMatcher;
    }

    @Override
    public boolean onCreate()
    {
        _uriHandlerFactory = new ContentUriHandlerFactory(s_uriMatcher, new MoviesDbHelper(getContext()));

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        return _uriHandlerFactory.getFor(uri).query(
            _uriHandlerFactory.uriSwitchFrom(uri),
            uri,
            projection,
            selection,
            selectionArgs,
            sortOrder
            );
    }

    @Override
    public String getType(Uri uri)
    {
        return _uriHandlerFactory.getMimeType(uri);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        return _uriHandlerFactory.getFor(uri).insert(
          _uriHandlerFactory.uriSwitchFrom(uri),
          uri,
          values
          );
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        return _uriHandlerFactory.getFor(uri).delete(
            _uriHandlerFactory.uriSwitchFrom(uri),
            uri,
            selection,
            selectionArgs
            );
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        return _uriHandlerFactory.getFor(uri).update(
            _uriHandlerFactory.uriSwitchFrom(uri),
            uri,
            values,
            selection,
            selectionArgs
            );
    }
}
