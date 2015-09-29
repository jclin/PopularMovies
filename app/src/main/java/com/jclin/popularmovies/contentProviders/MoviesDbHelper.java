package com.jclin.popularmovies.contentProviders;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public final class MoviesDbHelper extends SQLiteOpenHelper
{
    private static final String LOG_TAG = MoviesDbHelper.class.getName();

    private static final String DatabaseName = "movies.db";
    private static final int    Version      = 1;

    public MoviesDbHelper(Context context)
    {
        super(context, DatabaseName, null, Version);
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        Log.i(LOG_TAG, String.format("Creating %s database...", DatabaseName));

        String createTableQuery =
            "CREATE TABLE " + MoviesContract.FavoriteMovies.TableName + " (\n" +
            MovieColumns._ID + " INTEGER  NOT NULL PRIMARY KEY,\n" +
            MovieColumns.OriginalTitle + " TEXT  NOT NULL,\n" +
            MovieColumns.Overview + " TEXT  NOT NULL,\n" +
            MovieColumns.PosterPath + " TEXT  NOT NULL,\n" +
            MovieColumns.VoteAverage + " REAL  NOT NULL,\n" +
            MovieColumns.ReleaseDate + " INTEGER  NULL\n" +
            ");\n";

        database.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i(LOG_TAG, "Upgrading the database from version " + oldVersion + " to " + newVersion);
    }
}
