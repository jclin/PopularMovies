package com.jclin.popularmovies.contentProviders.contentUriHandlers;

import com.jclin.popularmovies.contentProviders.UriSwitches;
import com.jclin.popularmovies.data.SortOrder;

public final class HighlyRatedMoviesUriHandler extends TheMovieDbUriHandler
{
    public HighlyRatedMoviesUriHandler()
    {
        super(SortOrder.Rating);
    }

    @Override
    protected UriSwitches allMoviesUriSwitch()
    {
        return UriSwitches.HighlyRatedMovies;
    }

    @Override
    protected UriSwitches singleMovieUriSwitch()
    {
        return UriSwitches.HighlyRatedMovie;
    }
}
