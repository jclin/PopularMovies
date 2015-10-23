package com.jclin.popularmovies.contentProviders;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public final class TrailersContract
{
    public static String Authority    = "com.jclin.popularmovies.trailers";
    public static Uri BaseContentUri  = Uri.parse("content://" + Authority);

    public static final String TableName           = "trailers";
    public static final Uri ContentUri             = Uri.withAppendedPath(BaseContentUri, TableName);
    public static final String MOVIE_ID_BUNDLE_KEY = "MOVIE_ID";

    public static Uri contentUriFor(long movieID)
    {
        return ContentUris.withAppendedId(ContentUri, movieID);
    }

    public static final class Types
    {
        public static final String DirectoryType = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Authority + "/" + TableName;
    }

    public static final class Columns implements BaseColumns
    {
        public static final TrailerColumns YouTubeUrl = TrailerColumns.YouTubeUrl;
        public static final TrailerColumns Title      = TrailerColumns.Title;
        public static final String[] Projection       = TrailerColumns.projection();
    }

    private TrailersContract()
    {
    }

}
