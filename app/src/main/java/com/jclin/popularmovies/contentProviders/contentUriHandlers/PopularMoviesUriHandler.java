package com.jclin.popularmovies.contentProviders.contentUriHandlers;

import com.jclin.popularmovies.contentProviders.UriSwitches;
import com.jclin.popularmovies.data.SortOrder;

public class PopularMoviesUriHandler extends TheMovieDbUriHandler
{
    public PopularMoviesUriHandler()
    {
        super(SortOrder.Popularity);
    }

    @Override
    protected UriSwitches allMoviesUriSwitch()
    {
        return UriSwitches.PopularMovies;
    }

    @Override
    protected UriSwitches singleMovieUriSwitch()
    {
        return UriSwitches.PopularMovie;
    }
}
