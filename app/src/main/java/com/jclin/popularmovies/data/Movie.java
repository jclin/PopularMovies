package com.jclin.popularmovies.data;

import java.util.Date;

public final class Movie
{
    private final long   _id;
    private final String _originalTitle;
    private final String _overview;
    private final String _posterPath;
    private final double _voteAverage;
    private final Date   _releaseDate;

    public long getId()
    {
        return _id;
    }

    public String getOriginalTitle()
    {
        return _originalTitle;
    }

    public String getOverview()
    {
        return _overview;
    }

    public String getPosterPath()
    {
        return _posterPath;
    }

    public double getVoteAverage()
    {
        return _voteAverage;
    }

    public Date getRelaseDate()
    {
        return _releaseDate;
    }

    public Movie(
        long id,
        String originalTitle,
        String overview,
        String posterPath,
        double voteAverage,
        Date releaseDate
        )
    {
        _id             = id;
        _originalTitle  = originalTitle;
        _overview       = overview;
        _posterPath     = posterPath;
        _voteAverage    = voteAverage;
        _releaseDate    = releaseDate;
    }
}
