package com.jclin.popularmovies.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.jclin.popularmovies.R;
import com.jclin.popularmovies.activities.MovieDetailsActivity;
import com.jclin.popularmovies.data.FavoriteMovieQuery;
import com.jclin.popularmovies.data.ImageProvider;
import com.jclin.popularmovies.data.ImageSize;
import com.jclin.popularmovies.data.Movie;
import com.jclin.popularmovies.data.TheMovieDBUri;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class MovieDetailsActivityFragment extends Fragment
{
    private static final String LOG_TAG     = MovieDetailsActivity.class.getName();
    private static final String MOVIE_KEY   = "MOVIE_BUNDLE_KEY";

    protected @Bind(R.id.textView_movie_title)   TextView _movieTitleTextView;
    protected @Bind(R.id.textView_release_date)  TextView _releaseDateTextView;
    protected @Bind(R.id.textView_user_rating)   TextView _userRatingTextView;
    protected @Bind(R.id.textView_plot_synopsis) TextView _plotSynopsisTextView;
    protected @Bind(R.id.switch_favorite)        Switch   _switchFavorite;

    protected @Bind(R.id.root_linear_layout) LinearLayout _rootLinearLayout;

    private Movie _movie;

    public MovieDetailsActivityFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        ButterKnife.bind(this, view);

        _movie = getMovieFromIntent(savedInstanceState);

        populateControls(_movie);

        _rootLinearLayout
            .getViewTreeObserver()
            .addOnGlobalLayoutListener(new ImageViewHeightAdjuster(_rootLinearLayout, _movie));

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putParcelable(MOVIE_KEY, _movie);
    }

    @OnCheckedChanged(R.id.switch_favorite)
    protected void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        if (isChecked)
        {
            FavoriteMovieQuery.insert(_movie, getActivity().getContentResolver());
        }
        else
        {
            FavoriteMovieQuery.remove(_movie, getActivity().getContentResolver());
        }
    }

    private Movie getMovieFromIntent(Bundle savedInstanceState)
    {
        if (savedInstanceState != null)
        {
            Log.i(LOG_TAG, "Restoring Movie instance...");
            _movie = savedInstanceState.getParcelable(MOVIE_KEY);
        }

        return getActivity()
            .getIntent()
            .getParcelableExtra(getString(R.string.INTENT_DATA_MOVIE));
    }

    private void populateControls(Movie movie)
    {
        _movieTitleTextView.setText(movie.getOriginalTitle());
        _releaseDateTextView.setText(movie.getReleaseDateString());
        _userRatingTextView.setText(String.format("%.1f", movie.getVoteAverage()));
        _plotSynopsisTextView.setText(movie.getOverview());
        _switchFavorite.setChecked(FavoriteMovieQuery.alreadyExists(movie, getActivity().getContentResolver()));
    }

    private final class ImageViewHeightAdjuster implements ViewTreeObserver.OnGlobalLayoutListener
    {
        private final LinearLayout _rootLinearLayout;
        private final Movie _movie;

        public ImageViewHeightAdjuster(LinearLayout rootLinearLayout, Movie movie)
        {
            _rootLinearLayout = rootLinearLayout;
            _movie            = movie;
        }

        @Override
        public void onGlobalLayout()
        {
            ImageView imageView = (ImageView)_rootLinearLayout.findViewById(R.id.imageView_movie_poster);

            int pixelWidth = getActivity()
                    .getResources()
                    .getDimensionPixelSize(R.dimen.image_movie_poster_width);

            int pixelHeight = ImageSize.pixelHeightFrom(pixelWidth);

            if (imageView.getLayoutParams().height == pixelHeight)
            {
                return;
            }

            imageView.setLayoutParams(new LinearLayout.LayoutParams(pixelWidth, pixelHeight));

            ImageProvider.beginLoadFor(
                    TheMovieDBUri.buildForImage(_movie.getPosterPath()),
                    pixelWidth,
                    pixelHeight,
                    imageView
            );
        }
    }
}
