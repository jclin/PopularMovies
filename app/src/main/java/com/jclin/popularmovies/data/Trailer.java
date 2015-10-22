package com.jclin.popularmovies.data;

import android.net.Uri;

public final class Trailer
{
    private final Uri _youTubeVideoUri;
    private final String _title;
    private final long _id;

    public long id()
    {
        return _id;
    }

    public Uri youTubeUri()
    {
        return _youTubeVideoUri;
    }

    public String title()
    {
        return _title;
    }

    public Trailer(long id, String key, String title)
    {
        _id              = id;
        _youTubeVideoUri = UriBuilder.buildForYouTubeVideo(key);
        _title           = title;
    }
}
