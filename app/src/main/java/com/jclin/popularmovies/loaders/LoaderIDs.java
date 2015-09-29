package com.jclin.popularmovies.loaders;

import java.security.InvalidParameterException;

public enum LoaderIDs
{
    PopularMovies(100),
    HighlyRatedMovies(200),
    FavoriteMovies(300);

    private final int _id;

    public int value()
    {
        return _id;
    }

    LoaderIDs(int id)
    {
        _id = id;
    }

    public static LoaderIDs parse(int id)
    {
        if (PopularMovies.value() == id)
        {
            return PopularMovies;
        }

        if (HighlyRatedMovies.value() == id)
        {
            return HighlyRatedMovies;
        }

        if (FavoriteMovies.value() == id)
        {
            return FavoriteMovies;
        }

        throw new InvalidParameterException("Unknown loader id = " + id);
    }
}
