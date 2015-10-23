package com.jclin.popularmovies.contentProviders;

public abstract class MovieColumnsBase
{
    public static final MovieColumns _ID           = MovieColumns._ID;
    public static final MovieColumns OriginalTitle = MovieColumns.OriginalTitle;
    public static final MovieColumns Overview      = MovieColumns.Overview;
    public static final MovieColumns PosterPath    = MovieColumns.PosterPath;
    public static final MovieColumns VoteAverage   = MovieColumns.VoteAverage;
    public static final MovieColumns ReleaseDate   = MovieColumns.ReleaseDate;
    public static final String[] Projection        = MovieColumns.projection();
}
