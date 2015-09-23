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

import com.jclin.popularmovies.data.MovieProvider;
import com.jclin.popularmovies.data.Settings;
import com.jclin.popularmovies.data.SortOrder;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class MoviesActivityFragment extends Fragment
{
    private final String LOG_TAG            = MoviesActivityFragment.class.getName();
    private final String MOVIE_PROVIDER_TAG = "Movie_Provider";

    private ImageAdapter _imageAdapter;
    private MovieProvider _movieProvider;
    private ArrayAdapter<CharSequence> _sortingSpinnerAdapter;
    private ActionBar _actionBar;

    protected @Bind(R.id.gridView) GridView _gridView;

    public MoviesActivityFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        _movieProvider = restoreMovieProvider(savedInstanceState);
        _actionBar     = createActionBarSpinners();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View fragmentView = inflater.inflate(R.layout.fragment_movies, container, false);

        ButterKnife.bind(this, fragmentView);

        setupGridViewLayout();

        _actionBar.setListNavigationCallbacks(_sortingSpinnerAdapter, new ActionBar.OnNavigationListener()
        {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId)
            {
                String spinnerString = (String) _sortingSpinnerAdapter.getItem(itemPosition);
                if (updateSortSettingFrom(spinnerString))
                {
                    _imageAdapter.sortBy(Settings.getSortOrder());
                    return true;
                }

                return false;
            }
        });

        restoreActionBarSelectedItem(_actionBar);

        _imageAdapter = new ImageAdapter(
            getActivity(),
            _movieProvider,
            Settings.getSortOrder()
            );

        _gridView.setAdapter(_imageAdapter);

        return fragmentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putParcelable(MOVIE_PROVIDER_TAG, _movieProvider);
    }

    @OnItemClick(R.id.gridView)
    protected void onGridViewItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent movieDetailsIntent = new Intent(getActivity(), MovieDetailsActivity.class);
        movieDetailsIntent.putExtra(getString(R.string.INTENT_DATA_MOVIE), _imageAdapter.getMovie(position));

        startActivity(movieDetailsIntent);
    }

    private void setupGridViewLayout()
    {
        _gridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
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
                        (int) (_gridView.getWidth() / (float) (thumbnailPixelWidth + thumbnailPixelSpacing));

                if (numColumns > 0)
                {
                    final int columnWidth = (_gridView.getWidth() / numColumns) - thumbnailPixelSpacing;

                    _imageAdapter.setNumColumns(numColumns);
                    _imageAdapter.setItemPixelWidth(columnWidth);
                }
            }
        });
    }

    private ActionBar createActionBarSpinners()
    {
        AppCompatActivity activity = (AppCompatActivity)getActivity();

        Context themedContext = activity.getSupportActionBar().getThemedContext();
        if (themedContext == null)
        {
            Log.w(LOG_TAG, "Could not setup app bar spinners.");
            return null;
        }

        _sortingSpinnerAdapter = ArrayAdapter.createFromResource(
            themedContext,
            R.array.movie_sorting_values,
            R.layout.support_simple_spinner_dropdown_item
            );

        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        return actionBar;
    }

    private MovieProvider restoreMovieProvider(Bundle savedInstanceState)
    {
        if (savedInstanceState == null || !savedInstanceState.containsKey(MOVIE_PROVIDER_TAG))
        {
            Log.e(LOG_TAG, "Did you forget to save the movie provider?");
            return new MovieProvider();
        }

        return savedInstanceState.getParcelable(MOVIE_PROVIDER_TAG);
    }

    private boolean updateSortSettingFrom(String spinnerString)
    {
        if (spinnerString.equals(getResources().getString(R.string.spinner_popularity)))
        {
            Settings.setSortOrder(SortOrder.Popularity);
            return true;
        }

        if (spinnerString.equals(getResources().getString(R.string.spinner_rating)))
        {
            Settings.setSortOrder(SortOrder.Rating);
            return true;
        }

        return false;
    }

    private void restoreActionBarSelectedItem(ActionBar actionBar)
    {
        SortOrder sortOrder = Settings.getSortOrder();
        actionBar.setSelectedNavigationItem(sortOrder == SortOrder.Popularity ? 0 : 1);
    }
}
