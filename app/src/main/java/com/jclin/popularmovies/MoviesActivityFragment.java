package com.jclin.popularmovies;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

public class MoviesActivityFragment extends Fragment
{
    private ImageAdapter _imageAdapter;

    public MoviesActivityFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View fragmentView = inflater.inflate(R.layout.fragment_movies, container, false);

        GridView gridView = (GridView)fragmentView.findViewById(R.id.gridView);

        _imageAdapter = new ImageAdapter(getActivity());
        gridView.setAdapter(_imageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                throw new NotImplementedException();
            }
        });

        return fragmentView;
    }
}
