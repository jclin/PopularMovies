package com.jclin.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jclin.popularmovies.data.ImageSize;
import com.jclin.popularmovies.data.Movie;
import com.jclin.popularmovies.data.TheMovieDBImageUri;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

public class MovieDetailsActivityFragment extends Fragment
{
    public MovieDetailsActivityFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        Intent movieDetailsIntent = getActivity().getIntent();

        Movie movie = movieDetailsIntent.getParcelableExtra(getString(R.string.INTENT_DATA_MOVIE));

        populateControls(view, movie);

        LinearLayout rootLinearLayout = (LinearLayout)view.findViewById(R.id.gridView_root);
        rootLinearLayout
            .getViewTreeObserver()
            .addOnGlobalLayoutListener(new ImageViewHeightAdjuster(rootLinearLayout, movie));

        return view;
    }

    private void populateControls(View rootView, Movie movie)
    {
        ((TextView)rootView.findViewById(R.id.textView_movie_title)).setText(movie.getOriginalTitle());
        ((TextView)rootView.findViewById(R.id.textView_release_date)).setText(new SimpleDateFormat("yyyy-MM-dd").format(movie.getRelaseDate()));
        ((TextView)rootView.findViewById(R.id.textView_user_rating)).setText(String.format("%.1f", movie.getVoteAverage()));
        ((TextView)rootView.findViewById(R.id.textView_plot_synopsis)).setText(movie.getOverview());
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

            int pixelHeight = ImageSize.pixelHeightFrom(getActivity(), pixelWidth);

            if (imageView.getLayoutParams().height == pixelHeight)
            {
                return;
            }

            imageView.setLayoutParams(new LinearLayout.LayoutParams(pixelWidth, pixelHeight));

            Picasso
                .with(getActivity())
                .load(TheMovieDBImageUri.buildFor(_movie.getPosterPath()))
                .resize(pixelWidth, pixelHeight)
                .centerInside()
                .error(R.drawable.minions)
                .into(imageView);
        }
    }
}
