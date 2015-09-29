package com.jclin.popularmovies.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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

import com.jclin.popularmovies.R;
import com.jclin.popularmovies.adapters.ImageAdapter;
import com.jclin.popularmovies.data.Settings;
import com.jclin.popularmovies.data.SortOrder;
import com.jclin.popularmovies.loaders.LoaderFactory;
import com.jclin.popularmovies.loaders.LoaderIDs;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class MoviesActivityFragment
    extends Fragment
    implements ActionBar.OnNavigationListener, LoaderManager.LoaderCallbacks<Cursor>
{
    private final String LOG_TAG = MoviesActivityFragment.class.getName();

    private ImageAdapter _imageAdapter;
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

        _actionBar = createActionBarSpinners();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View fragmentView = inflater.inflate(R.layout.fragment_movies, container, false);

        ButterKnife.bind(this, fragmentView);

        setupGridViewLayout();

        _actionBar.setListNavigationCallbacks(_sortingSpinnerAdapter, this);
        restoreActionBarSelectedItem(_actionBar);

        _imageAdapter = new ImageAdapter(getActivity(), null, 0);
        _gridView.setAdapter(_imageAdapter);

        return fragmentView;
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId)
    {
        String spinnerString = (String) _sortingSpinnerAdapter.getItem(itemPosition);
        if (updateSortSettingFrom(spinnerString))
        {
            initLoaderBy(Settings.getSortOrder());

            return true;
        }

        return false;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        return LoaderFactory.createFor(LoaderIDs.parse(id), getActivity(), args);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        _imageAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        _imageAdapter.swapCursor(null);
    }

    @OnItemClick(R.id.gridView)
    protected void onGridViewItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        // TODO: re-implement launch of movie details activity
//        Intent movieDetailsIntent = new Intent(getActivity(), MovieDetailsActivity.class);
//        movieDetailsIntent.putExtra(getString(R.string.INTENT_DATA_MOVIE), _imageAdapter.getMovie(position));
//
//        startActivity(movieDetailsIntent);
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

                final int thumbnailPixelWidth   = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_width);
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

        initLoaderBy(sortOrder);
    }

    private void initLoaderBy(SortOrder sortOrder)
    {
        getActivity()
            .getSupportLoaderManager()
            .initLoader(sortOrder.loaderID().value(), null, this);
    }
}
