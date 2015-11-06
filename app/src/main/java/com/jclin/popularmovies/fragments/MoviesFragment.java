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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jclin.popularmovies.R;
import com.jclin.popularmovies.adapters.ImageAdapter;
import com.jclin.popularmovies.data.Movie;
import com.jclin.popularmovies.data.Settings;
import com.jclin.popularmovies.data.SortOrder;
import com.jclin.popularmovies.loaders.LoaderFactory;
import com.jclin.popularmovies.loaders.LoaderIDs;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class MoviesFragment
    extends Fragment
    implements ActionBar.OnNavigationListener, LoaderManager.LoaderCallbacks<Cursor>
{
    private final String LOG_TAG = MoviesFragment.class.getName();

    private ImageAdapter _imageAdapter;
    private ArrayAdapter<CharSequence> _sortingSpinnerAdapter;
    private ActionBar _actionBar;

    protected @Bind(R.id.gridView)                   GridView _gridView;
    protected @Bind(R.id.progressBar_movies_loading) ProgressBar _moviesLoadingProgressBar;
    protected @Bind(R.id.textView_no_movies)         TextView _noMoviesTextView;

    public interface Callbacks
    {
        void onMovieSelected(Movie movie);

        void onSelectionCleared();
    }

    public MoviesFragment()
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
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        ButterKnife.bind(this, rootView);

        setupGridViewLayout();

        _actionBar.setListNavigationCallbacks(_sortingSpinnerAdapter, this);
        restoreActionBarSelectedItem(_actionBar);

        _imageAdapter = new ImageAdapter(getActivity(), null, 0);
        _gridView.setAdapter(_imageAdapter);

        return rootView;
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId)
    {
        String spinnerString = (String) _sortingSpinnerAdapter.getItem(itemPosition);
        if (updateSortSettingFrom(spinnerString))
        {
            clearGridViewSelection();

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
        if (SortOrder.from(LoaderIDs.parse(loader.getId())) != Settings.getSortOrder())
        {
            Log.i(LOG_TAG, "The cursor will not be swapped in, b/c it is not for the current sort order.");
            return;
        }

        updateViewsFrom(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        _imageAdapter.swapCursor(null);
    }

    @OnItemClick(R.id.gridView)
    protected void onGridViewItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        notifyActivityOnMovieSelected((Movie) view.getTag());
    }

    private void notifyActivityOnMovieSelected(Movie movie)
    {
        ((Callbacks)getActivity()).onMovieSelected(movie);
    }

    private void notifyActivityOnSelectionCleared()
    {
        ((Callbacks) getActivity()).onSelectionCleared();
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

    private boolean updateSortSettingFrom(String spinnerString)
    {
        if (spinnerString.equals(Settings.getSortOrder().toString()))
        {
            Log.i(LOG_TAG, "spinnerString '" + spinnerString + "' doesn't change the current setting.");
            return false;
        }

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

        if (spinnerString.equals(getResources().getString(R.string.spinner_favorites)))
        {
            Settings.setSortOrder(SortOrder.Favorites);
            return true;
        }

        return false;
    }

    private void restoreActionBarSelectedItem(ActionBar actionBar)
    {
        SortOrder sortOrder = Settings.getSortOrder();
        actionBar.setSelectedNavigationItem(sortOrder.index());

        initLoaderBy(sortOrder);
    }

    private void initLoaderBy(SortOrder sortOrder)
    {
        showLoadingProgress();

        getLoaderManager().initLoader(
            LoaderIDs.from(sortOrder).id(),
            null,
            this
            );
    }

    private void showLoadingProgress()
    {
        _moviesLoadingProgressBar.setVisibility(View.VISIBLE);
        _gridView.setVisibility(View.GONE);
        _noMoviesTextView.setVisibility(View.GONE);
    }

    private void hideLoadingProgress()
    {
        _moviesLoadingProgressBar.setVisibility(View.GONE);
        _gridView.setVisibility(View.VISIBLE);
    }

    private void updateViewsFrom(Cursor data)
    {
        hideLoadingProgress();

        _imageAdapter.swapCursor(data);

        _gridView.setEmptyView(_noMoviesTextView);
    }

    private void clearGridViewSelection()
    {
        _gridView.clearChoices();
        notifyActivityOnSelectionCleared();
    }
}
