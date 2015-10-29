package com.jclin.popularmovies.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jclin.popularmovies.R;
import com.jclin.popularmovies.adapters.ReviewAdapter;
import com.jclin.popularmovies.contentProviders.ReviewsContract;
import com.jclin.popularmovies.loaders.LoaderFactory;
import com.jclin.popularmovies.loaders.LoaderIDs;

import butterknife.Bind;
import butterknife.ButterKnife;

public final class MovieReviewsActivityFragment
    extends Fragment
    implements LoaderManager.LoaderCallbacks<Cursor>
{
    protected @Bind(R.id.listView_reviews) ListView _reviewsListView;
    protected @Bind(R.id.progressBar_reviews_loading) ProgressBar _reviewsLoadingProgressBar;
    protected @Bind(R.id.textView_no_reviews) TextView _noReviewsTextView;

    private long _movieId;
    private CursorAdapter _reviewsAdapter;

    public MovieReviewsActivityFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_movie_reviews, container, false);

        ButterKnife.bind(this, rootView);

        _movieId = getMovieIdFromIntent(savedInstanceState);
        initReviewsLoaderFor(_movieId);

        _reviewsAdapter = new ReviewAdapter(getActivity(), null, 0);
        _reviewsListView.setAdapter(_reviewsAdapter);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        return LoaderFactory.createFor(LoaderIDs.parse(id), getActivity(), args);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        hideLoadingProgress();

        _reviewsAdapter.swapCursor(data);

        _reviewsListView.setEmptyView(_noReviewsTextView);
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
        showLoadingProgress();

        Bundle movieIdArgs = new Bundle();
        movieIdArgs.putLong(ReviewsContract.MOVIE_ID_BUNDLE_KEY, movieId);

        getActivity().getSupportLoaderManager().initLoader(
            LoaderIDs.MovieReviews.id(),
            movieIdArgs,
            this
            );
    }

    private void showLoadingProgress()
    {
        _reviewsLoadingProgressBar.setVisibility(View.VISIBLE);
        _reviewsListView.setVisibility(View.GONE);
        _noReviewsTextView.setVisibility(View.GONE);
    }

    private void hideLoadingProgress()
    {
        _reviewsLoadingProgressBar.setVisibility(View.GONE);
        _reviewsListView.setVisibility(View.VISIBLE);
    }
}
