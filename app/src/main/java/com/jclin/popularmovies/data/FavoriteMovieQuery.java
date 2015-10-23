package com.jclin.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.jclin.popularmovies.contentProviders.MoviesContract;

public final class FavoriteMovieQuery
{
    private static final String LOG_TAG = FavoriteMovieQuery.class.getName();

    private FavoriteMovieQuery()
    {
    }

    public static boolean alreadyExists(Movie movie, ContentResolver contentResolver)
    {
        Cursor cursor = contentResolver.query(
            MoviesContract.FavoriteMovies.contentUriFor(movie.getId()),
            MoviesContract.FavoriteMovies.Columns.Projection,
            null,
            null,
            null
            );

        boolean isInFavorites = cursor.moveToFirst();
        cursor.close();

        return isInFavorites;
    }

    public static void remove(Movie movie, ContentResolver contentResolver)
    {
        Log.i(LOG_TAG, "Removing movie from favorites. ID = " + movie.getId());

        int rowsDeleted = contentResolver.delete(
            MoviesContract.FavoriteMovies.contentUriFor(movie.getId()),
            null,
            null
            );

        Log.i(LOG_TAG, rowsDeleted + " rows deleted.");
    }

    public static void insert(Movie movie, ContentResolver contentResolver)
    {
        Log.i(LOG_TAG, "Inserting movie into favorites. ID = " + movie.getId());

        ContentValues contentValues = new ContentValues();

        contentValues.put(MoviesContract.FavoriteMovies.Columns._ID, movie.getId());
        contentValues.put(MoviesContract.FavoriteMovies.Columns.OriginalTitle, movie.getOriginalTitle());
        contentValues.put(MoviesContract.FavoriteMovies.Columns.Overview, movie.getOverview());
        contentValues.put(MoviesContract.FavoriteMovies.Columns.PosterPath, movie.getPosterPath());
        contentValues.put(MoviesContract.FavoriteMovies.Columns.VoteAverage, movie.getVoteAverage());
        contentValues.put(MoviesContract.FavoriteMovies.Columns.ReleaseDate, movie.getReleaseDateMilliseconds());

        contentResolver.insert(
                MoviesContract.FavoriteMovies.contentUriFor(movie.getId()),
                contentValues
        );
    }

}
