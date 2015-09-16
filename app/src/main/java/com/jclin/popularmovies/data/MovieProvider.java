package com.jclin.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public final class MovieProvider implements Parcelable, OnMoviesRetrievedListener
{
    public static final Parcelable.Creator<MovieProvider> CREATOR = new MovieProviderCreator();

    private static final String LOG_TAG = MovieProvider.class.getName();

    private final ArrayList<Movie> _cachedPopularMovies;
    private final ArrayList<Movie> _cachedHighestRatedMovies;

    private SortOrder _sortOrder;
    private GetMoviesTask _getMoviesTask;
    private OnMoviesRetrievedListener _moviesRetrievedListener;

    public MovieProvider()
    {
        _cachedPopularMovies      = new ArrayList<>();
        _cachedHighestRatedMovies = new ArrayList<>();
    }

    private MovieProvider(Parcel source)
    {
        _cachedPopularMovies      = source.createTypedArrayList(Movie.CREATOR);
        _cachedHighestRatedMovies = source.createTypedArrayList(Movie.CREATOR);
        _sortOrder                = SortOrder.valueOf(source.readString());
    }

    public int getCount()
    {
        return getDisplayMovies().size();
    }

    public Movie getAt(int position)
    {
        return getDisplayMovies().get(position);
    }

    public SortOrder getSortOrder()
    {
        return _sortOrder;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeTypedList(_cachedPopularMovies);
        dest.writeTypedList(_cachedHighestRatedMovies);
        dest.writeString(_sortOrder.name());
    }

    @Override
    public void onMoviesRetrieved(Movie[] movies, SortOrder sortedBy)
    {
        if (_moviesRetrievedListener == null)
        {
            return;
        }

        _moviesRetrievedListener.onMoviesRetrieved(movies, sortedBy);
    }

    public void setOnMoviesRetrievedListener(OnMoviesRetrievedListener listener)
    {
        _moviesRetrievedListener = listener;
    }

    public void sortBy(SortOrder sortOrder)
    {
        if (_sortOrder == sortOrder)
        {
            return;
        }

        _sortOrder = sortOrder;

        ArrayList<Movie> displayMovies = getDisplayMovies();
        if (displayMovies.size() > 0)
        {
            Log.i(LOG_TAG, String.format("sortBy: %s has already been retrieved. Using cached instances...", sortOrder));
            onMoviesRetrieved(displayMovies.toArray(new Movie[displayMovies.size()]), sortOrder);
            return;
        }

        fetchMovies(_sortOrder);
    }

    private void fetchMovies(SortOrder sortOrder)
    {
        switch (sortOrder)
        {
            case Popularity:
                cancel(_getMoviesTask);
                _getMoviesTask = new GetMoviesTask(SortOrder.Popularity);
                Log.i(LOG_TAG, "Fetching movies by popularity...");
                break;

            case Rating:
                cancel(_getMoviesTask);
                _getMoviesTask = new GetMoviesTask(SortOrder.Rating);
                Log.i(LOG_TAG, "Fetching movies by rating...");
                break;
        }

        _getMoviesTask.setOnMoviesRetrievedListener(new OnMoviesRetrievedListener()
        {
            @Override
            public void onMoviesRetrieved(Movie[] movies, SortOrder sortedBy)
            {
                Collections.addAll(
                    sortedBy == SortOrder.Popularity ? _cachedPopularMovies : _cachedHighestRatedMovies,
                    movies
                );

                MovieProvider.this.onMoviesRetrieved(movies, sortedBy);
            }
        });

        _getMoviesTask.execute();
    }

    private void cancel(GetMoviesTask getMoviesTask)
    {
        if (getMoviesTask == null)
        {
            return;
        }

        switch (getMoviesTask.getStatus())
        {
            case PENDING:
            case RUNNING:
                Log.i(LOG_TAG, "Cancelling existing task...");
                getMoviesTask.cancel(true);
                break;
        }
    }

    private ArrayList<Movie> getDisplayMovies()
    {
        return _sortOrder == SortOrder.Popularity ?
            _cachedPopularMovies :
            _cachedHighestRatedMovies;
    }

    private static final class MovieProviderCreator implements Parcelable.Creator<MovieProvider>
    {
        @Override
        public MovieProvider createFromParcel(Parcel source)
        {
            return new MovieProvider(source);
        }

        @Override
        public MovieProvider[] newArray(int size)
        {
            return new MovieProvider[size];
        }
    }
}
