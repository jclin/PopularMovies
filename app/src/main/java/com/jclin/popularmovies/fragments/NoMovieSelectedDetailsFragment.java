package com.jclin.popularmovies.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jclin.popularmovies.R;

public final class NoMovieSelectedDetailsFragment extends Fragment
{
    public NoMovieSelectedDetailsFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_no_movie_selected_details, container, false);
    }
}
