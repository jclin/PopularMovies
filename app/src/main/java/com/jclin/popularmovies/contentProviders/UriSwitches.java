package com.jclin.popularmovies.contentProviders;

public enum UriSwitches
{
    FavoriteMovies(100),
    FavoriteMovie(101),
    PopularMovies(200),
    PopularMovie(201),
    HighlyRatedMovies(300),
    HighlyRatedMovie(301),
    Unknown(Integer.MAX_VALUE);

    private final int _value;

    public int value()
    {
        return _value;
    }

    UriSwitches(int value)
    {
        _value = value;
    }

    public static UriSwitches parse(int value)
    {
        switch (value)
        {
            case 100:
                return FavoriteMovies;

            case 101:
                return FavoriteMovie;

            case 200:
                return PopularMovies;

            case 201:
                return PopularMovie;

            case 300:
                return HighlyRatedMovies;

            case 301:
                return HighlyRatedMovie;

            default:
                return Unknown;
        }
    }
}
