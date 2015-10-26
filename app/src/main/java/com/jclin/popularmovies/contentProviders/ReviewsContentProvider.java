package com.jclin.popularmovies.contentProviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import com.jclin.popularmovies.data.Review;
import com.jclin.popularmovies.data.TheMovieDbApi;

import java.util.Hashtable;

public final class ReviewsContentProvider extends ContentProvider
{
    private static final String LOG_TAG = ReviewsContentProvider.class.getName();

    private static final UriMatcher s_uriMatcher = buildUriMatcher();

    private Hashtable<Long, Review[]> _retrievedReviews = new Hashtable<>();

    private static UriMatcher buildUriMatcher()
    {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(ReviewsContract.Authority, ReviewsContract.TableName + "/#", UriSwitches.Reviews.value());

        return uriMatcher;
    }

    @Override
    public boolean onCreate()
    {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        UriSwitches uriSwitch = UriSwitches.parse(s_uriMatcher.match(uri));
        if (uriSwitch != UriSwitches.Reviews)
        {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        final long movieId = Long.valueOf(uri.getLastPathSegment());

        Cursor cursor = buildCursorFor(fetchReviewsFor(movieId));

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri)
    {
        switch (UriSwitches.parse(s_uriMatcher.match(uri)))
        {
            case Reviews:
                return ReviewsContract.Types.DirectoryType;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        Log.i(LOG_TAG, "Inserting is not supported. Skipping...");

        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        Log.i(LOG_TAG, "Deleting is not supported. Skipping...");

        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        Log.i(LOG_TAG, "Updating is not supported. Skipping...");

        return 0;
    }

    private Review[] fetchReviewsFor(long movieId)
    {
        if (_retrievedReviews.containsKey(movieId))
        {
            Log.i(LOG_TAG, "Reviews already retrieved for movie with id '" + movieId + "'");
            return _retrievedReviews.get(movieId);
        }

        Log.i(LOG_TAG, "Fetching reviews for movie with id '" + movieId + "' from The Movie DB...");
        _retrievedReviews.put(movieId, TheMovieDbApi.fetchReviewsFor(movieId));

        return _retrievedReviews.get(movieId);
    }

    private Cursor buildCursorFor(Review[] reviews)
    {
        MatrixCursor matrixCursor = new MatrixCursor(ReviewsContract.Columns.Projection);
        for (Review review : reviews)
        {
            matrixCursor.addRow(
                new Object[]
                {
                    review.id(),
                    review.author(),
                    review.content()
                }
            );
        }

        return matrixCursor;
    }
}
