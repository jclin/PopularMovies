package com.jclin.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.jclin.popularmovies.data.GetMoviesTask;
import com.jclin.popularmovies.data.Movie;

import java.util.ArrayList;

public class MoviesActivityFragment extends Fragment
{
    private final String LOG_TAG          = MoviesActivityFragment.class.getName();
    private final String MOVIES_STATE_TAG = "Movies";

    private ImageAdapter _imageAdapter;
    private ArrayList<Movie> _cachedMovies;

    public MoviesActivityFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setupAppBarSpinners();

        _cachedMovies = restoreMoviesState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View fragmentView = inflater.inflate(R.layout.fragment_movies, container, false);

        GridView gridView = (GridView) fragmentView.findViewById(R.id.gridView);

        setupGridViewLayout(gridView);

        _imageAdapter = new ImageAdapter(getActivity(), new GetMoviesTask(), _cachedMovies);

        gridView.setAdapter(_imageAdapter);
        gridView.setOnItemClickListener(new OnItemClickListener());

        return fragmentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(MOVIES_STATE_TAG, _imageAdapter.getMovies());
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

                final int thumbnailPixelWidth = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_width);
                final int thumbnailPixelSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);

                final int numColumns =
                        (int) (gridView.getWidth() / (float) (thumbnailPixelWidth + thumbnailPixelSpacing));

                if (numColumns > 0)
                {
                    final int columnWidth = (gridView.getWidth() / numColumns) - thumbnailPixelSpacing;

                    _imageAdapter.setNumColumns(numColumns);
                    _imageAdapter.setItemPixelWidth(columnWidth);
                }
            }
        });
    }

    private void setupAppBarSpinners()
    {
        AppCompatActivity activity = (AppCompatActivity)getActivity();

        Context themedContext = activity.getSupportActionBar().getThemedContext();
        if (themedContext == null)
        {
            Log.w(LOG_TAG, "Could not setup app bar spinners.");
            return;
        }

        ArrayAdapter<CharSequence> sortingSpinnerAdapter = ArrayAdapter.createFromResource(
                themedContext,
                R.array.movie_sorting_values,
                R.layout.support_simple_spinner_dropdown_item
        );

        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(sortingSpinnerAdapter, new ActionBar.OnNavigationListener()
        {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId)
            {
                // TODO: Execute query on The Movie DB
                return false;
            }
        });
    }

    private ArrayList<Movie> restoreMoviesState(Bundle savedInstanceState)
    {
        if (savedInstanceState == null || !savedInstanceState.containsKey(MOVIES_STATE_TAG))
        {
            Log.e(LOG_TAG, "Did you forget to save the list of retrieved movies?");
            return new ArrayList<>();
        }

        return savedInstanceState.getParcelableArrayList(MOVIES_STATE_TAG);
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
