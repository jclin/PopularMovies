package com.jclin.popularmovies.contentProviders;

public enum MovieColumns
{
    _ID(0, "_id"),
    OriginalTitle(1, "originaltitle"),
    Overview(2, "overview"),
    PosterPath(3, "posterpath"),
    VoteAverage(4, "voteaverage"),
    ReleaseDate(5, "releasedate");

    private final int _index;
    private final String _name;

    public int getIndex()
    {
        return _index;
    }

    public String getName()
    {
        return _name;
    }

    MovieColumns(int index, String name)
    {
        _index = index;
        _name  = name;
    }

    public static String[] projection()
    {
        return new String[]
        {
            _ID.getName(),
            OriginalTitle.getName(),
            Overview.getName(),
            PosterPath.getName(),
            VoteAverage.getName(),
            ReleaseDate.getName()
        };
    }
};
