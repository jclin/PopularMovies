package com.jclin.popularmovies.data;

import android.net.Uri;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

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
        OkHttpClient httpClient = new OkHttpClient();

        Request okHttpRequest = new Request.Builder()
                .url(request.toString())
                .build();

        try
        {
            Response response = httpClient.newCall(okHttpRequest).execute();

            return response.body().string();
        }
        catch (IOException e)
        {
            Log.w(LOG_TAG, "Error retrieving url ", e);
            return NO_RESPONSE;
        }
    }
}
