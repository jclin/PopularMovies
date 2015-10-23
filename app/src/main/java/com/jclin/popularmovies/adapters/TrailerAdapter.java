package com.jclin.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jclin.popularmovies.R;
import com.jclin.popularmovies.contentProviders.TrailersContract;

public final class TrailerAdapter extends CursorAdapter
{
    public TrailerAdapter(Context context, Cursor cursor, int flags)
    {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        return ((LayoutInflater)context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
            .inflate(R.layout.listview_item_trailerview, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        TextView titleTextView = (TextView) view.findViewById(R.id.textView_trailer_title);
        titleTextView.setText(cursor.getString(TrailersContract.Columns.Title.getIndex()));

        view.setTag(cursor.getString(TrailersContract.Columns.YouTubeUrl.getIndex()));
    }
}
