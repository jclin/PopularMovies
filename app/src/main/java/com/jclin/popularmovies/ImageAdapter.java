package com.jclin.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.jclin.popularmovies.data.ImageProvider;
import com.jclin.popularmovies.data.ImageSize;
import com.jclin.popularmovies.data.Movie;
import com.jclin.popularmovies.data.MovieProvider;
import com.jclin.popularmovies.data.OnMoviesRetrievedListener;
import com.jclin.popularmovies.data.SortOrder;
import com.jclin.popularmovies.data.TheMovieDBUri;

public final class ImageAdapter extends BaseAdapter
{
    private final Context _context;
    private final MovieProvider _movieProvider;

    private int _numColumns      = 0;
    private int _itemPixelWidth  = 0;
    private int _itemPixelHeight = 0;

    private AbsListView.LayoutParams _imageViewLayoutParams;

    public int getNumColumns()
    {
        return _numColumns;
    }

    public void setNumColumns(int numColumns)
    {
        _numColumns = numColumns;
        notifyDataSetChanged();
    }

    public ImageAdapter(Context context, MovieProvider movieProvider, SortOrder sortOrder)
    {
        _context        = context;
        _movieProvider  = movieProvider;

        _movieProvider.setOnMoviesRetrievedListener(new OnMoviesRetrievedListener()
        {
            @Override
            public void onMoviesRetrieved(Movie[] movies, SortOrder sortedBy)
            {
                notifyDataSetChanged();
            }
        });

        _movieProvider.sortBy(sortOrder);
    }

    @Override
    public int getCount()
    {
        return _movieProvider.getCount();
    }

    @Override
    public Object getItem(int position)
    {
        return _movieProvider.getAt(position);
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

        ImageProvider.beginLoadFor(
            TheMovieDBUri.buildForImage(_movieProvider.getAt(position).getPosterPath()),
            _itemPixelWidth,
            _itemPixelHeight,
            imageView
        );

        return imageView;
    }

    public void setItemPixelWidth(int itemPixelWidth)
    {
        if (_itemPixelWidth == itemPixelWidth)
        {
            return;
        }

        _itemPixelWidth  = itemPixelWidth;
        _itemPixelHeight = ImageSize.pixelHeightFrom(itemPixelWidth);

        _imageViewLayoutParams = new GridView.LayoutParams(_itemPixelWidth, _itemPixelHeight);

        notifyDataSetChanged();
    }

    public Movie getMovie(int position)
    {
        return _movieProvider.getAt(position);
    }

    public void sortBy(SortOrder sortOrder)
    {
        if (_movieProvider.getSortOrder() == sortOrder)
        {
            return;
        }

        _movieProvider.sortBy(sortOrder);
        notifyDataSetChanged();
    }
}
