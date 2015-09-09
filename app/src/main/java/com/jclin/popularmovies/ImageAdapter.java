package com.jclin.popularmovies;

import android.content.Context;
import android.net.Uri;
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
    private final Context _context;
    private final GetMoviesTask _getMoviesTask;

    private int _numColumns      = 0;
    private int _itemPixelWidth  = 0;
    private int _itemPixelHeight = 0;

    private ArrayList<Movie> _movies = new ArrayList<>();

    private AbsListView.LayoutParams _imageViewLayoutParams;

    public int getNumColumns()
    {
        return _numColumns;
    }

    public void setNumColumns(int numColumns)
    {
        _numColumns = numColumns;
    }

    public ImageAdapter(Context context, GetMoviesTask getMoviesTask)
    {
        _context       = context;
        _getMoviesTask = getMoviesTask;

        _getMoviesTask.setOnMoviesRetrievedListener(new GetMoviesTask.OnMoviesRetrievedListener()
        {
            @Override
            public void onMoviesRetrieved(Movie[] movies)
            {
                Collections.addAll(_movies, movies);
                notifyDataSetChanged();
            }
        });

        _getMoviesTask.execute(SortOrder.Popularity);
    }

    @Override
    public int getCount()
    {
        return _movies.size();
    }

    @Override
    public Object getItem(int position)
    {
        return R.drawable.minions;
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

        Picasso
                .with(_context)
                .load(TheMovieDBImageUri.buildFor(_movies.get(position).getPosterPath()))
                .resize(_itemPixelWidth, _itemPixelHeight)
                .centerInside()
                .error(R.drawable.minions)
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
}
