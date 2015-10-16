package com.jclin.popularmovies.loaders;

import com.jclin.popularmovies.data.SortOrder;

import java.security.InvalidParameterException;

public enum LoaderIDs
{
    PopularMovies(100, SortOrder.Popularity),
    HighlyRatedMovies(200, SortOrder.Rating),
    FavoriteMovies(300, SortOrder.Favorites);

    private final int _id;
    private final SortOrder _sortOrder;

    public int id()
    {
        return _id;
    }

    LoaderIDs(int id, SortOrder sortOrder)
    {
        _id        = id;
        _sortOrder = sortOrder;
    }

    public SortOrder sortOrder()
    {
        return _sortOrder;
    }

    public static LoaderIDs parse(int id)
    {
        if (PopularMovies.id() == id)
        {
            return PopularMovies;
        }

        if (HighlyRatedMovies.id() == id)
        {
            return HighlyRatedMovies;
        }

        if (FavoriteMovies.id() == id)
        {
            return FavoriteMovies;
        }

        throw new InvalidParameterException("Unknown loader id = " + id);
    }
}
