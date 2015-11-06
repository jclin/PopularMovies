package com.jclin.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.jclin.popularmovies.R;
import com.jclin.popularmovies.contentProviders.MovieColumns;
import com.jclin.popularmovies.data.ImageProvider;
import com.jclin.popularmovies.data.ImageSize;
import com.jclin.popularmovies.data.Movie;
import com.jclin.popularmovies.data.UriBuilder;

public final class ImageAdapter extends CursorAdapter
{
    private int _numColumns      = 0;
    private int _itemPixelWidth  = 0;
    private int _itemPixelHeight = 0;

    private AbsListView.LayoutParams _frameLayoutLayoutParams;

    public int getNumColumns()
    {
        return _numColumns;
    }

    public void setNumColumns(int numColumns)
    {
        _numColumns = numColumns;
        notifyDataSetChanged();
    }

    public ImageAdapter(Context context, Cursor cursor, int flags)
    {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        FrameLayout frameLayout = (FrameLayout) ((LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
            .inflate(R.layout.gridview_item_imageview, parent, false);

        tryUpdateLayoutParams(frameLayout);

        return frameLayout;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        FrameLayout frameLayout = (FrameLayout) view;

        if (!tryUpdateLayoutParams(frameLayout))
        {
            return;
        }

        Movie movie = makeMovie(cursor);
        frameLayout.setTag(movie);

        ImageProvider.beginLoadFor(
            UriBuilder.buildForImage(movie.getPosterPath()),
            _itemPixelWidth,
            _itemPixelHeight,
            (ImageView) frameLayout.findViewById(R.id.gridview_item_imageView)
            );
    }

    public void setItemPixelWidth(int itemPixelWidth)
    {
        if (_itemPixelWidth == itemPixelWidth)
        {
            return;
        }

        _itemPixelWidth  = itemPixelWidth;
        _itemPixelHeight = ImageSize.pixelHeightFrom(itemPixelWidth);

        _frameLayoutLayoutParams = new GridView.LayoutParams(_itemPixelWidth, _itemPixelHeight);

        notifyDataSetChanged();
    }

    private boolean tryUpdateLayoutParams(FrameLayout frameLayout)
    {
        if (_frameLayoutLayoutParams == null)
        {
            return false;
        }

        frameLayout.setLayoutParams(_frameLayoutLayoutParams);

        if (frameLayout.getLayoutParams().height != _itemPixelHeight)
        {
            frameLayout.setLayoutParams(_frameLayoutLayoutParams);
        }

        return true;
    }

    private Movie makeMovie(Cursor cursor)
    {
        return new Movie(
            cursor.getLong(MovieColumns._ID.getIndex()),
            cursor.getString(MovieColumns.OriginalTitle.getIndex()),
            cursor.getString(MovieColumns.Overview.getIndex()),
            cursor.getString(MovieColumns.PosterPath.getIndex()),
            cursor.getDouble(MovieColumns.VoteAverage.getIndex()),
            cursor.getLong(MovieColumns.ReleaseDate.getIndex())
            );
    }
}
