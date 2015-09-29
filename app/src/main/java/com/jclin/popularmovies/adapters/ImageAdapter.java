package com.jclin.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;

import com.jclin.popularmovies.R;
import com.jclin.popularmovies.contentProviders.MovieColumns;
import com.jclin.popularmovies.data.ImageProvider;
import com.jclin.popularmovies.data.ImageSize;
import com.jclin.popularmovies.data.TheMovieDBUri;

public final class ImageAdapter extends CursorAdapter
{
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

    public ImageAdapter(Context context, Cursor cursor, int flags)
    {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        ImageView imageView = (ImageView) ((LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
            .inflate(R.layout.gridview_item_imageview, parent, false);

        tryUpdateLayoutParams(imageView);

        return imageView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        ImageView imageView = (ImageView) view;

        if (!tryUpdateLayoutParams(imageView))
        {
            return;
        }

        String posterPath = cursor.getString(MovieColumns.PosterPath.getIndex());

        ImageProvider.beginLoadFor(
                TheMovieDBUri.buildForImage(posterPath),
                _itemPixelWidth,
                _itemPixelHeight,
                imageView
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

        _imageViewLayoutParams = new GridView.LayoutParams(_itemPixelWidth, _itemPixelHeight);

        notifyDataSetChanged();
    }

    private boolean tryUpdateLayoutParams(ImageView imageView)
    {
        if (_imageViewLayoutParams == null)
        {
            return false;
        }

        imageView.setLayoutParams(_imageViewLayoutParams);

        if (imageView.getLayoutParams().height != _itemPixelHeight)
        {
            imageView.setLayoutParams(_imageViewLayoutParams);
        }

        return true;
    }
}
