package com.jclin.popularmovies;

import android.app.Application;
import android.content.Context;

public final class App extends Application
{
    private static Context _context;

    public static Context getContext()
    {
        return _context;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        _context = this;
    }
}
