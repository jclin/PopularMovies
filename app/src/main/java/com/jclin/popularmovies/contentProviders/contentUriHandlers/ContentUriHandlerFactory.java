package com.jclin.popularmovies.contentProviders.contentUriHandlers;

import android.content.Context;
import android.content.UriMatcher;
import android.net.Uri;

import com.jclin.popularmovies.contentProviders.MoviesContract;
import com.jclin.popularmovies.contentProviders.MoviesDbHelper;
import com.jclin.popularmovies.contentProviders.UriSwitches;

import java.util.Hashtable;

public class ContentUriHandlerFactory
{
    private final Context _context;
    private final UriMatcher _uriMatcher;
    private final MoviesDbHelper _moviesDbHelper;
    private final Hashtable<UriSwitches, IContentUriHandler> _uriHandlers;

    public ContentUriHandlerFactory(Context context, UriMatcher uriMatcher, MoviesDbHelper moviesDbHelper)
    {
        _context        = context;
        _uriMatcher     = uriMatcher;
        _moviesDbHelper = moviesDbHelper;
        _uriHandlers    = new Hashtable<>();

        addUriHandlers();
    }

    public IContentUriHandler getFor(Uri uri)
    {
        UriSwitches uriSwitch = uriSwitchFrom(uri);
        if (!_uriHandlers.containsKey(uriSwitch))
        {
            throw new UnsupportedOperationException("Unknown uri = " + uri);
        }

        return _uriHandlers.get(uriSwitch);
    }

    public String getMimeType(Uri uri)
    {
        switch (UriSwitches.parse(_uriMatcher.match(uri)))
        {
            case FavoriteMovies:
                return MoviesContract.FavoriteMovies.Types.DirectoryType;

            case FavoriteMovie:
                return MoviesContract.FavoriteMovies.Types.ItemType;

            case PopularMovies:
                return MoviesContract.PopularMovies.Types.DirectoryType;

            case PopularMovie:
                return MoviesContract.PopularMovies.Types.ItemType;

            case HighlyRatedMovies:
                return MoviesContract.HighlyRatedMovies.Types.DirectoryType;

            case HighlyRatedMovie:
                return MoviesContract.HighlyRatedMovies.Types.ItemType;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    public UriSwitches uriSwitchFrom(Uri uri)
    {
        return UriSwitches.parse(_uriMatcher.match(uri));
    }

    private void addUriHandlers()
    {
        FavoriteMoviesUriHandler faveMoviesUriHandler =
                new FavoriteMoviesUriHandler(_context, _moviesDbHelper);
        _uriHandlers.put(UriSwitches.FavoriteMovies, faveMoviesUriHandler);
        _uriHandlers.put(UriSwitches.FavoriteMovie, faveMoviesUriHandler);

        PopularMoviesUriHandler popularMoviesUriHandler =
                new PopularMoviesUriHandler();
        _uriHandlers.put(UriSwitches.PopularMovies, popularMoviesUriHandler);
        _uriHandlers.put(UriSwitches.PopularMovie, popularMoviesUriHandler);

        HighlyRatedMoviesUriHandler highlyRatedMoviesUriHandler =
                new HighlyRatedMoviesUriHandler();
        _uriHandlers.put(UriSwitches.HighlyRatedMovies, highlyRatedMoviesUriHandler);
        _uriHandlers.put(UriSwitches.HighlyRatedMovie, highlyRatedMoviesUriHandler);
    }
}
