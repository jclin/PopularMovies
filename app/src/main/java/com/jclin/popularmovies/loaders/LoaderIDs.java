package com.jclin.popularmovies.loaders;

import com.jclin.popularmovies.data.SortOrder;

import java.security.InvalidParameterException;

public enum LoaderIDs
{
    PopularMovies(100),
    HighlyRatedMovies(200),
    FavoriteMovies(300),
    MovieTrailers(400),
    MovieReviews(500);

    private final int _id;

    public int id()
    {
        return _id;
    }

    LoaderIDs(int id)
    {
        _id = id;
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

        if (MovieTrailers.id() == id)
        {
            return MovieTrailers;
        }

        if (MovieReviews.id() == id)
        {
            return MovieReviews;
        }

        throw new InvalidParameterException("Unknown loader id = " + id);
    }

    public static LoaderIDs from(SortOrder sortOrder)
    {
        switch (sortOrder)
        {
            case Popularity:
                return PopularMovies;

            case Favorites:
                return FavoriteMovies;

            case Rating:
                return HighlyRatedMovies;

            default:
                throw new InvalidParameterException("No loader ID for sort order = " + sortOrder);
        }
    }
}
