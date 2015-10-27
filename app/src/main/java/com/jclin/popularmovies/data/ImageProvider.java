package com.jclin.popularmovies.data;

import android.net.Uri;
import android.widget.ImageView;

import com.jclin.popularmovies.App;
import com.jclin.popularmovies.R;
import com.squareup.picasso.Picasso;

public final class ImageProvider
{
    private ImageProvider()
    {
    }

    public static void beginLoadFor(
        Uri imageUri,
        int pixelWidth,
        int pixelHeight,
        ImageView targetView
        )
    {
        Picasso
            .with(App.getContext())
            .load(imageUri)
            .resize(pixelWidth, pixelHeight)
            .centerInside()
            .error(R.drawable.error_fetch_movie_poster)
            .into(targetView);
    }
}
