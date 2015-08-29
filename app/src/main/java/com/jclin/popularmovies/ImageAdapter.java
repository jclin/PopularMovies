package com.jclin.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public final class ImageAdapter extends BaseAdapter
{
    private final Context _context;

    public ImageAdapter(Context context)
    {
        _context = context;
    }

    @Override
    public int getCount()
    {
        return 12;
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
            convertView = ((LayoutInflater)_context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.gridview_item_imageview, parent, false);
        }

        ImageView imageView = (ImageView) convertView;
        imageView.setImageResource(R.drawable.minions);

        return imageView;
    }
}
