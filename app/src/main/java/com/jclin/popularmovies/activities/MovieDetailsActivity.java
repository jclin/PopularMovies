package com.jclin.popularmovies.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jclin.popularmovies.R;
import com.jclin.popularmovies.data.FragmentFactory;
import com.jclin.popularmovies.data.Movie;

public class MovieDetailsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        if (savedInstanceState != null)
        {
            return;
        }

        getSupportFragmentManager()
            .beginTransaction()
            .replace(
                R.id.movie_details_layout_container,
                FragmentFactory.buildMovieDetailsFragment(getMovieFromIntent()))
            .commit();
    }

    private Movie getMovieFromIntent()
    {
        return getIntent().getParcelableExtra(getString(R.string.INTENT_DATA_MOVIE));
    }
}
