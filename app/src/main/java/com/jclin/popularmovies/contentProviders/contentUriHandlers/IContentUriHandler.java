package com.jclin.popularmovies.contentProviders.contentUriHandlers;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.jclin.popularmovies.contentProviders.UriSwitches;

public interface IContentUriHandler
{
    Cursor query(
        UriSwitches uriSwitch,
        Uri rawUri,
        String[] projection,
        String selection,
        String[] selectionArgs,
        String sortOrder
        );

    Uri insert(
        UriSwitches uriSwitch,
        Uri rawUri,
        ContentValues values
        );

    int update(
        UriSwitches uriSwitch,
        Uri rawUri,
        ContentValues values,
        String selection,
        String[] selectionArgs
        );

    int delete(
        UriSwitches uriSwitch,
        Uri rawUri,
        String selection,
        String[] selectionArgs
        );
}
