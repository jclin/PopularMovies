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
        // TODO: turn off indicators when ready to submit
        Picasso picasso = Picasso.with(App.getContext());

        picasso.setIndicatorsEnabled(true);

        picasso.load(imageUri)
            .resize(pixelWidth, pixelHeight)
            .centerInside()
            .placeholder(R.drawable.image_loading_placeholder)
            .error(R.drawable.error_fetch_movie_poster)
            .into(targetView);
    }
}
