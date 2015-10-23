package com.jclin.popularmovies.contentProviders;

import android.provider.BaseColumns;

public abstract class MovieColumnsBase implements BaseColumns
{
    public static final String OriginalTitle = MovieColumns.OriginalTitle.getName();
    public static final String Overview      = MovieColumns.Overview.getName();
    public static final String PosterPath    = MovieColumns.PosterPath.getName();
    public static final String VoteAverage   = MovieColumns.VoteAverage.getName();
    public static final String ReleaseDate   = MovieColumns.ReleaseDate.getName();
    public static final String[] Projection  = MovieColumns.projection();
}
