package com.jclin.popularmovies;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.jclin.popularmovies.data.GetMoviesTask;

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

        GridView gridView = (GridView) fragmentView.findViewById(R.id.gridView);

        setupGridViewLayout(gridView);

        _imageAdapter = new ImageAdapter(getActivity(), new GetMoviesTask());
        gridView.setAdapter(_imageAdapter);

        gridView.setOnItemClickListener(new OnItemClickListener());

        return fragmentView;
    }

    private void setupGridViewLayout(final GridView gridView)
    {
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
            if (_imageAdapter.getNumColumns() > 0)
            {
                return;
            }

            final int thumbnailPixelWidth   = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_width);
            final int thumbnailPixelSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);

            final int numColumns =
                (int) (gridView.getWidth() / (float)(thumbnailPixelWidth + thumbnailPixelSpacing));

            if (numColumns > 0)
            {
                final int columnWidth = (gridView.getWidth() / numColumns) - thumbnailPixelSpacing;

                _imageAdapter.setNumColumns(numColumns);
                _imageAdapter.setItemPixelWidth(columnWidth);
            }
            }
        });
    }

    private final class OnItemClickListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            Intent movieDetailsIntent = new Intent(getActivity(), MovieDetailsActivity.class);
            movieDetailsIntent.putExtra(getString(R.string.INTENT_DATA_MOVIE), _imageAdapter.getMovie(position));

            startActivity(movieDetailsIntent);
        }
    }
}
