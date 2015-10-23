package com.jclin.popularmovies.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jclin.popularmovies.R;
import com.jclin.popularmovies.fragments.MovieDetailsActivityFragment;

public class MoviesActivity extends AppCompatActivity
{
    private static final String MOVIE_DETAILS_FRAGMENT_TAG = "MOVIE_DETAILS_FRAGMENT";

    private boolean _twoPaneLayout;

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
