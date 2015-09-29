package com.jclin.popularmovies.data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

public final class TheMovieDbApi
{
    private static final String LOG_TAG = TheMovieDbApi.class.getName();

    private TheMovieDbApi()
    {
    }

    public static Hashtable<Long, Movie> fetchMovies(SortOrder sortOrder)
    {
        String jsonResponse = HttpRequester.send(TheMovieDBUri.buildForMovies(sortOrder));

        JSONObject jsonObj = toJsonObject(jsonResponse);
        if (jsonObj == null)
        {
            return new Hashtable<>(0);
        }

        return parseMovies(jsonObj);
    }

    private static JSONObject toJsonObject(String rawJson)
    {
        JSONObject jsonObj = null;
        try
        {
            jsonObj = new JSONObject(rawJson);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error making JSON object", e);
        }

        return jsonObj;
    }

    private static Hashtable<Long, Movie> parseMovies(JSONObject jsonObj)
    {
        Hashtable<Long, Movie> movies = new Hashtable<>();
        try
        {
            JSONArray results = jsonObj.getJSONArray(MovieJsonTags.TAG_RESULTS);
            for (int i = 0; i < results.length(); i++)
            {
                JSONObject movieObj = results.getJSONObject(i);

                Movie movie = new Movie(
                    movieObj.getLong(MovieJsonTags.TAG_ID),
                    ValidatedJson.parseString(movieObj, MovieJsonTags.TAG_ORIGINAL_TITLE),
                    ValidatedJson.parseString(movieObj, MovieJsonTags.TAG_OVERVIEW),
                    ValidatedJson.parseString(movieObj, MovieJsonTags.TAG_POSTER_PATH).replace("/", ""),
                    movieObj.getDouble(MovieJsonTags.TAG_VOTE_AVERAGE),
                    ValidatedJson.parseDate(movieObj, MovieJsonTags.TAG_RELEASE_DATE)
                    );

                movies.put(movie.getId(), movie);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error with JSON object", e);
        }

        return movies;
    }
}
