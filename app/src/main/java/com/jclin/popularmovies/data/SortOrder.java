package com.jclin.popularmovies.data;

import android.content.Context;

import com.jclin.popularmovies.R;
import com.jclin.popularmovies.loaders.LoaderIDs;

public enum SortOrder
{
    Popularity(0)
    {
        @Override
        public String getQueryParam()
        {
            return "popularity.desc";
        }

        @Override
        public String toSettingString(Context context)
        {
            return context.getResources().getString(R.string.setting_sort_by_popularity);
        }

        @Override
        public LoaderIDs loaderID()
        {
            return LoaderIDs.PopularMovies;
        }
    },

    Rating(1)
    {
        @Override
        public String getQueryParam()
        {
            return "vote_average.desc";
        }

        @Override
        public String toSettingString(Context context)
        {
            return context.getResources().getString(R.string.setting_sort_by_rating);
        }

        @Override
        public LoaderIDs loaderID()
        {
            return LoaderIDs.HighlyRatedMovies;
        }
    },

    Favorites(2)
    {
        @Override
        public String getQueryParam()
        {
            return "";
        }

        @Override
        public String toSettingString(Context context)
        {
            return context.getResources().getString(R.string.setting_sort_by_favorites);
        }

        @Override
        public LoaderIDs loaderID()
        {
            return LoaderIDs.FavoriteMovies;
        }
    };

    private final int _index;

    SortOrder(int index)
    {
        _index = index;
    }

    public int index()
    {
        return _index;
    }

    public abstract String getQueryParam();
    public abstract String toSettingString(Context context);
    public abstract LoaderIDs loaderID();

    public static SortOrder fromSettingString(Context context, String settingString)
    {
        if (settingString.equals(context.getResources().getString(R.string.setting_sort_by_popularity)))
        {
            return SortOrder.Popularity;
        }

        if (settingString.equals(context.getResources().getString(R.string.setting_sort_by_rating)))
        {
            return SortOrder.Rating;
        }

        if (settingString.equals(context.getResources().getString(R.string.setting_sort_by_favorites)))
        {
            return SortOrder.Favorites;
        }

        throw new IllegalArgumentException("settingString is an unknown sortBy setting string");
    }
}
