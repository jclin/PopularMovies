package com.jclin.popularmovies.data;

import android.content.Context;
import android.preference.PreferenceManager;

import com.jclin.popularmovies.App;
import com.jclin.popularmovies.R;

public final class Settings
{
    private Settings()
    {
    }

    public static SortOrder getSortOrder()
    {
        Context context = App.getContext();

        String sortSettingString = PreferenceManager
            .getDefaultSharedPreferences(context)
            .getString(
                context.getResources().getString(R.string.setting_sort_by_key),
                context.getResources().getString(R.string.setting_sort_by_popularity)
            );

        return SortOrder.fromSettingString(sortSettingString);
    }

    public static void setSortOrder(SortOrder orderBy)
    {
        Context context = App.getContext();

        PreferenceManager
            .getDefaultSharedPreferences(context)
            .edit()
            .putString(
                context.getResources().getString(R.string.setting_sort_by_key),
                orderBy.toSettingString())
            .commit();
    }
}
