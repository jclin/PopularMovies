package com.jclin.popularmovies.contentProviders;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ReviewsContract
{
    public static String Authority    = "com.jclin.popularmovies.reviews";
    public static Uri BaseContentUri  = Uri.parse("content://" + Authority);

    public static final String TableName           = "reviews";
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
        public static final ReviewColumns Author  = ReviewColumns.Author;
        public static final ReviewColumns Content = ReviewColumns.Content;
        public static final String[] Projection   = ReviewColumns.projection();
    }
}
