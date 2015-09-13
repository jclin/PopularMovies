package com.jclin.popularmovies.data;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class HttpRequester
{
    private static final String LOG_TAG     = "Http Requester";
    private static final String NO_RESPONSE = "";

    private HttpRequester()
    {
    }

    public static String send(Uri request)
    {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try
        {
            urlConnection = (HttpURLConnection) new URL(request.toString()).openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();

            StringBuilder stringBuilder = new StringBuilder();
            if (inputStream == null)
            {
                return NO_RESPONSE;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null)
            {
                stringBuilder.append(line).append("\n");
            }

            if (stringBuilder.length() == 0)
            {
                return NO_RESPONSE;
            }

            return stringBuilder.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error opening an HTTP connection", e);
        }
        finally
        {
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }

            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (final IOException e)
                {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return NO_RESPONSE;
    }
}
