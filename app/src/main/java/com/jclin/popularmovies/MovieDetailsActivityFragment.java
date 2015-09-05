package com.jclin.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jclin.popularmovies.data.Movie;

import java.text.SimpleDateFormat;

public class MovieDetailsActivityFragment extends Fragment
{
    private final String LOG_TAG = MovieDetailsActivityFragment.class.getName();

    public MovieDetailsActivityFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        Intent movieDetailsIntent = getActivity().getIntent();
        if (movieDetailsIntent == null ||
            !movieDetailsIntent.hasExtra(getString(R.string.INTENT_DATA_MOVIE)))
        {
            Log.e(LOG_TAG, "Oops, did you forget to put a Movie into the intent?");
            return view;
        }

        Movie movie = movieDetailsIntent.getParcelableExtra(getString(R.string.INTENT_DATA_MOVIE));

        populateControls(view, movie);

        return view;
    }

    private void populateControls(View rootView, Movie movie)
    {
        ((TextView)rootView.findViewById(R.id.textView_movie_title)).setText(movie.getOriginalTitle());
        ((TextView)rootView.findViewById(R.id.textView_release_date)).setText(new SimpleDateFormat("yyyy-MM-dd").format(movie.getRelaseDate()));
        ((TextView)rootView.findViewById(R.id.textView_user_rating)).setText(String.format("%.1f", movie.getVoteAverage()));
        ((TextView)rootView.findViewById(R.id.textView_plot_synopsis)).setText(movie.getOverview());
    }
}
