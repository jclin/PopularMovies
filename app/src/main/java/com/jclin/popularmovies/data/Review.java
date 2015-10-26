package com.jclin.popularmovies.data;

public final class Review
{
    private final long _id;
    private final String _author;
    private final String _content;

    public long id()
    {
        return _id;
    }

    public String author()
    {
        return _author;
    }

    public String content()
    {
        return _content;
    }

    public Review(long id, String author, String content)
    {
        _id      = id;
        _author  = author;
        _content = content;
    }
}
