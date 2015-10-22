package com.jclin.popularmovies.contentProviders;

public enum TrailerColumns
{
    _ID(0, "_id"),
    YouTubeUrl(1, "youtube_url"),
    Title(2, "title");

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

    TrailerColumns(int index, String name)
    {
        _index = index;
        _name  = name;
    }

    public static String[] projection()
    {
        return new String[]
        {
            _ID.getName(),
            YouTubeUrl.getName(),
            Title.getName()
        };
    }
}
