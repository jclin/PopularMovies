package com.jclin.popularmovies.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jclin.popularmovies.R;
import com.jclin.popularmovies.fragments.MovieDetailsActivityFragment;

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
            .add(R.id.movie_details_layout_container, new MovieDetailsActivityFragment())
            .commit();
    }
}
