package com.jclin.popularmovies.contentProviders;

public enum ReviewColumns
{
    _ID(0, "_id"),
    Author(1, "author"),
    Content(2, "content");

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

    ReviewColumns(int index, String name)
    {
        _index = index;
        _name  = name;
    }

    public static String[] projection()
    {
        return new String[]
        {
            _ID.getName(),
            Author.getName(),
            Content.getName()
        };
    }
}
