package com.jclin.popularmovies.data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class ValidatedJson
{
    private static final String NULL_STR = "null";
    private static final String LOG_TAG = ValidatedJson.class.getName();

    private ValidatedJson()
    {
    }

    public static String parseString(JSONObject jsonObject, String name) throws JSONException
    {
        String str = jsonObject.getString(name);
        return str.compareToIgnoreCase(NULL_STR) == 0 ?
            "" :
            str;
    }

    public static Date parseDate(JSONObject jsonObject, String name) throws JSONException
    {
        String dateString = jsonObject.getString(name);

        if (dateString.compareToIgnoreCase(NULL_STR) == 0)
        {
            return null;
        }

        Date date = null;
        try
        {
            date = new SimpleDateFormat(MovieConsts.DATE_FORMAT, Locale.US).parse(dateString);
        }
        catch (ParseException e)
        {
            Log.e(LOG_TAG, "Error parsing the date string", e);
        }

        return date;
    }
}
