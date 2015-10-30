package com.jclin.popularmovies.data;

import android.content.Intent;

import com.jclin.popularmovies.App;
import com.jclin.popularmovies.R;

public final class TrailerShareIntent
{
    private TrailerShareIntent()
    {
    }

    public static Intent buildWith(String movieName, String body)
    {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        shareIntent.putExtra(
            Intent.EXTRA_SUBJECT,
            String.format(
                App.getContext().getString(R.string.share_trailer_title_format),
                movieName
                )
            );

        shareIntent.putExtra(Intent.EXTRA_TEXT, body);

        return shareIntent;
    }
}
