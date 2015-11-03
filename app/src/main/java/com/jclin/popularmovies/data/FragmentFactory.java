package com.jclin.popularmovies.data;

import android.os.Bundle;

import com.jclin.popularmovies.App;
import com.jclin.popularmovies.R;
import com.jclin.popularmovies.fragments.MovieDetailsActivityFragment;

public final class FragmentFactory
{
    private FragmentFactory()
    {
    }

    public static MovieDetailsActivityFragment buildMovieDetailsFragment(Movie movie)
    {
        final Bundle fragmentArgs = new Bundle();
        fragmentArgs.putParcelable(App.getContext().getString(R.string.INTENT_DATA_MOVIE), movie);

        MovieDetailsActivityFragment movieDetailsFragment = new MovieDetailsActivityFragment();
        movieDetailsFragment.setArguments(fragmentArgs);

        return movieDetailsFragment;
    }
}
