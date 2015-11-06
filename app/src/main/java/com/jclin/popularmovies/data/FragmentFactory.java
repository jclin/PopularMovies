package com.jclin.popularmovies.data;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.jclin.popularmovies.App;
import com.jclin.popularmovies.R;
import com.jclin.popularmovies.fragments.MovieDetailsFragment;
import com.jclin.popularmovies.fragments.NoMovieSelectedDetailsFragment;

public final class FragmentFactory
{
    public static Fragment buildMovieDetailsFragment(Movie movie)
    {
        final Bundle fragmentArgs = new Bundle();
        fragmentArgs.putParcelable(App.getContext().getString(R.string.INTENT_DATA_MOVIE), movie);

        MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
        movieDetailsFragment.setArguments(fragmentArgs);

        return movieDetailsFragment;
    }

    public static Fragment buildNoMovieDetailsFragment()
    {
        return new NoMovieSelectedDetailsFragment();
    }

    private FragmentFactory()
    {
    }
}
