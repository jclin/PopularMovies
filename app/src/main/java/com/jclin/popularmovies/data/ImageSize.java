package com.jclin.popularmovies.data;

import android.content.Context;

import com.jclin.popularmovies.R;

public final class ImageSize
{
    private ImageSize()
    {
    }

    public static int pixelHeightFrom(Context context, int pixelWidth)
    {
        return (pixelWidth * context.getResources().getInteger(R.integer.image_thumbnail_pixel_height)) /
                context.getResources().getInteger(R.integer.image_thumbnail_pixel_width);
    }
}
