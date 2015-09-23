package com.jclin.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jclin.popularmovies.data.ImageProvider;
import com.jclin.popularmovies.data.ImageSize;
import com.jclin.popularmovies.data.Movie;
import com.jclin.popularmovies.data.TheMovieDBUri;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieDetailsActivityFragment extends Fragment
{
    protected @Bind(R.id.textView_movie_title)   TextView _movieTitleTextView;
    protected @Bind(R.id.textView_release_date)  TextView _releaseDateTextView;
    protected @Bind(R.id.textView_user_rating)   TextView _userRatingTextView;
    protected @Bind(R.id.textView_plot_synopsis) TextView _plotSynopsisTextView;

    protected @Bind(R.id.root_linear_layout) LinearLayout _rootLinearLayout;

    public MovieDetailsActivityFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        ButterKnife.bind(this, view);

        Movie movie = getMovieFromIntent();

        populateControls(movie);

        _rootLinearLayout
            .getViewTreeObserver()
            .addOnGlobalLayoutListener(new ImageViewHeightAdjuster(_rootLinearLayout, movie));

        return view;
    }

    private Movie getMovieFromIntent()
    {
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
