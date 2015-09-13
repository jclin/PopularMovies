package com.jclin.popularmovies.data;

import android.content.Context;

import com.jclin.popularmovies.App;
import com.jclin.popularmovies.R;

public final class ImageSize
{
    private ImageSize()
    {
    }

    public static int pixelHeightFrom(int pixelWidth)
    {
        return (pixelWidth * App.getContext().getResources().getInteger(R.integer.image_thumbnail_pixel_height)) /
                App.getContext().getResources().getInteger(R.integer.image_thumbnail_pixel_width);
    }
}
