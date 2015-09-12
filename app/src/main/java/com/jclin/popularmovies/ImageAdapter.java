package com.jclin.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.jclin.popularmovies.data.GetMoviesTask;
import com.jclin.popularmovies.data.ImageSize;
import com.jclin.popularmovies.data.Movie;
import com.jclin.popularmovies.data.SortOrder;
import com.jclin.popularmovies.data.TheMovieDBImageUri;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public final class ImageAdapter extends BaseAdapter
{
    private final String LOG_TAG = ImageAdapter.class.getName();

    private final Context _context;
    private final ArrayList<Movie> _movies;

    private SortOrder _sortOrder;
    private GetMoviesTask _getMoviesTask;

    private int _numColumns      = 0;
    private int _itemPixelWidth  = 0;
    private int _itemPixelHeight = 0;

    private AbsListView.LayoutParams _imageViewLayoutParams;

    public ArrayList<Movie> getMovies()
    {
        return _movies;
    }

    public int getNumColumns()
    {
        return _numColumns;
    }

    public void setNumColumns(int numColumns)
    {
        _numColumns = numColumns;
        notifyDataSetChanged();
    }

    public ImageAdapter(Context context, ArrayList<Movie> movies, SortOrder sortOrder)
    {
        _context   = context;
        _sortOrder = sortOrder;

        _movies = (movies != null) ? movies : new ArrayList<Movie>();
        if (_movies.size() == 0)
        {
            fetchMovies(_sortOrder);
        }
    }

    @Override
    public int getCount()
    {
        return _movies.size();
    }

    @Override
    public Object getItem(int position)
    {
        return _movies.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = ((LayoutInflater) _context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.gridview_item_imageview, parent, false);
        }

        ImageView imageView = (ImageView) convertView;

        if (_imageViewLayoutParams == null)
        {
            return imageView;
        }

        imageView.setLayoutParams(_imageViewLayoutParams);

        if (imageView.getLayoutParams().height != _itemPixelHeight)
        {
            imageView.setLayoutParams(_imageViewLayoutParams);
        }

        // TODO: turn off indicators when ready to submit
        Picasso picasso = Picasso.with(_context);

        picasso.setIndicatorsEnabled(true);

        picasso.load(TheMovieDBImageUri.buildFor(_movies.get(position).getPosterPath()))
            .resize(_itemPixelWidth, _itemPixelHeight)
            .centerInside()
            .error(R.drawable.error_fetch_movie_poster)
            .into(imageView);

        return imageView;
    }

    public void setItemPixelWidth(int itemPixelWidth)
    {
        if (_itemPixelWidth == itemPixelWidth)
        {
            return;
        }

        _itemPixelWidth  = itemPixelWidth;
        _itemPixelHeight = ImageSize.pixelHeightFrom(_context, itemPixelWidth);

        _imageViewLayoutParams = new GridView.LayoutParams(_itemPixelWidth, _itemPixelHeight);

        notifyDataSetChanged();
    }

    public Movie getMovie(int position)
    {
        return _movies.get(position);
    }

    public void sortBy(SortOrder sortOrder)
    {
        if (sortOrder == _sortOrder)
        {
            return;
        }

        _movies.clear();

        _sortOrder = sortOrder;
        fetchMovies(_sortOrder);

        notifyDataSetChanged();
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

        _getMoviesTask.setOnMoviesRetrievedListener(new GetMoviesTask.OnMoviesRetrievedListener()
        {
            @Override
            public void onMoviesRetrieved(Movie[] movies)
            {
                Collections.addAll(_movies, movies);
                notifyDataSetChanged();
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
                break;
        }
    }
}
