package com.jclin.popularmovies.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.jclin.popularmovies.R;
import com.jclin.popularmovies.adapters.ReviewAdapter;
import com.jclin.popularmovies.contentProviders.ReviewsContract;
import com.jclin.popularmovies.loaders.LoaderFactory;
import com.jclin.popularmovies.loaders.LoaderIDs;

public final class MovieReviewsActivityFragment
    extends Fragment
    implements LoaderManager.LoaderCallbacks<Cursor>
{
    private long _movieId;
    private CursorAdapter _reviewsAdapter;

    public MovieReviewsActivityFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ListView listView = (ListView) inflater.inflate(R.layout.fragment_movie_reviews, container, false);

        _movieId = getMovieIdFromIntent(savedInstanceState);
        initReviewsLoaderFor(_movieId);

        _reviewsAdapter = new ReviewAdapter(getActivity(), null, 0);
        listView.setAdapter(_reviewsAdapter);

        return listView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        return LoaderFactory.createFor(LoaderIDs.parse(id), getActivity(), args);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        _reviewsAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        _reviewsAdapter.swapCursor(null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putLong(ReviewsContract.MOVIE_ID_BUNDLE_KEY, _movieId);
    }

    private long getMovieIdFromIntent(Bundle savedInstanceState)
    {
        if (savedInstanceState == null)
        {
            return getActivity()
                .getIntent()
                .getLongExtra(ReviewsContract.MOVIE_ID_BUNDLE_KEY, -1);
        }

        return savedInstanceState.getLong(ReviewsContract.MOVIE_ID_BUNDLE_KEY);
    }

    private void initReviewsLoaderFor(long movieId)
    {
        Bundle movieIdArgs = new Bundle();
        movieIdArgs.putLong(ReviewsContract.MOVIE_ID_BUNDLE_KEY, movieId);

        getActivity().getSupportLoaderManager().initLoader(
            LoaderIDs.MovieReviews.id(),
            movieIdArgs,
            this
            );
    }
}
