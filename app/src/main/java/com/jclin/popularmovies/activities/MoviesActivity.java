package com.jclin.popularmovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jclin.popularmovies.R;
import com.jclin.popularmovies.data.FragmentFactory;
import com.jclin.popularmovies.data.Movie;
import com.jclin.popularmovies.fragments.MovieDetailsActivityFragment;
import com.jclin.popularmovies.fragments.MoviesActivityFragment;

public class MoviesActivity extends AppCompatActivity implements MoviesActivityFragment.Callbacks
{
    private static final String MOVIE_DETAILS_FRAGMENT_TAG = "MOVIE_DETAILS_FRAGMENT";

    private boolean _twoPaneLayout;

    @Override
    public void onMovieSelected(Movie movie)
    {
        if (_twoPaneLayout)
        {
            getSupportFragmentManager()
                .beginTransaction()
                .replace(
                    R.id.movie_details_layout_container,
                    FragmentFactory.buildMovieDetailsFragment(movie))
                .commit();

            return;
        }

        Intent movieDetailsIntent = new Intent(this, MovieDetailsActivity.class);
        movieDetailsIntent.putExtra(getString(R.string.INTENT_DATA_MOVIE), movie);

        startActivity(movieDetailsIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        _twoPaneLayout = findViewById(R.id.movie_details_layout_container) != null;

        if (!_twoPaneLayout || savedInstanceState != null)
        {
            return;
        }

        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.movie_details_layout_container, new MovieDetailsActivityFragment(), MOVIE_DETAILS_FRAGMENT_TAG)
            .commit();
    }
}
