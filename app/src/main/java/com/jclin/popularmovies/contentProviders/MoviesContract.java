package com.jclin.popularmovies.contentProviders;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;

public final class MoviesContract
{
    public static final String Authority    = "com.jclin.popularmovies";
    public static final Uri BaseContentUri  = Uri.parse("content://" + Authority);

    private MoviesContract()
    {
    }

    public static final class PopularMovies
    {
        public static String TableName     = "popular_movies";
        public static final Uri ContentUri = Uri.withAppendedPath(BaseContentUri, TableName);

        public static final class Types
        {
            public static final String DirectoryType = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Authority + "/" + TableName;
            public static final String ItemType      = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Authority + "/" + TableName;
        }

        public static final class Columns extends MovieColumnsBase
        {
        }

        public static Uri contentUriFor(long movieId)
        {
            return ContentUris.withAppendedId(ContentUri, movieId);
        }
    }

    public static final class HighlyRatedMovies
    {
        public static String TableName     = "highly_rated_movies";
        public static final Uri ContentUri = Uri.withAppendedPath(BaseContentUri, TableName);

        public static final class Types
        {
            public static final String DirectoryType = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Authority + "/" + TableName;
            public static final String ItemType      = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Authority + "/" + TableName;
        }

        public static final class Columns extends MovieColumnsBase
        {
        }

        public static Uri contentUriFor(long movieId)
        {
            return ContentUris.withAppendedId(ContentUri, movieId);
        }
    }

    public static final class FavoriteMovies
    {
        public static String TableName     = "favorite_movies";
        public static final Uri ContentUri = Uri.withAppendedPath(BaseContentUri, TableName);

        public static final class Types
        {
            public static final String DirectoryType = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Authority + "/" + TableName;
            public static final String ItemType      = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Authority + "/" + TableName;
        }

        public static final class Columns extends MovieColumnsBase
        {
        }

        public static Uri contentUriFor(long movieId)
        {
            return ContentUris.withAppendedId(ContentUri, movieId);
        }
    }
}
