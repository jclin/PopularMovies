package com.jclin.popularmovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jclin.popularmovies.R;
import com.jclin.popularmovies.data.FragmentFactory;
import com.jclin.popularmovies.data.Movie;
import com.jclin.popularmovies.fragments.MoviesFragment;

public class MoviesActivity extends AppCompatActivity implements MoviesFragment.Callbacks
{
    private boolean _twoPaneLayout;

    @Override
    public void onMovieSelected(Movie movie)
    {
        if (_twoPaneLayout)
        {
            loadMovieDetailsFragment(movie);
            return;
        }

        Intent movieDetailsIntent = new Intent(this, MovieDetailsActivity.class);
        movieDetailsIntent.putExtra(getString(R.string.INTENT_DATA_MOVIE), movie);

        startActivity(movieDetailsIntent);
    }

    @Override
    public void onSelectionCleared()
    {
        if (!_twoPaneLayout)
        {
            return;
        }

        loadNoMovieDetailsFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        _twoPaneLayout = findViewById(R.id.movie_details_layout_container) != null;

        if (savedInstanceState == null && _twoPaneLayout)
        {
            loadNoMovieDetailsFragment();
        }
    }

    private void loadMovieDetailsFragment(Movie movie)
    {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(
                R.id.movie_details_layout_container,
                FragmentFactory.buildMovieDetailsFragment(movie))
            .commit();
    }

    private void loadNoMovieDetailsFragment()
    {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(
                R.id.movie_details_layout_container,
                FragmentFactory.buildNoMovieDetailsFragment())
            .commit();
    }
}
