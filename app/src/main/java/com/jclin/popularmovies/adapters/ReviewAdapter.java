package com.jclin.popularmovies.adapters;

import android.app.Service;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.jclin.popularmovies.R;
import com.jclin.popularmovies.contentProviders.ReviewsContract;

public final class ReviewAdapter extends CursorAdapter
{
    public ReviewAdapter(Context context, Cursor c, int flags)
    {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        View view = ((LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE)).inflate(
            R.layout.listview_item_reviewview,
            parent,
            false
            );

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.userNameTextView = (TextView) view.findViewById(R.id.textView_username);
        viewHolder.contentTextView  = (TextView) view.findViewById(R.id.textView_review_content);

        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.userNameTextView.setText(cursor.getString(ReviewsContract.Columns.Author.getIndex()));
        viewHolder.contentTextView.setText(cursor.getString(ReviewsContract.Columns.Content.getIndex()));
    }

    private static final class ViewHolder
    {
        TextView userNameTextView;
        TextView contentTextView;
    }
}
