package com.jclin.popularmovies.data;

import android.util.Log;

import com.jclin.popularmovies.App;
import com.jclin.popularmovies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;

public final class TheMovieDbApi
{
    private static final String LOG_TAG = TheMovieDbApi.class.getName();

    private TheMovieDbApi()
    {
    }

    public static Hashtable<Long, Movie> fetchMovies(SortOrder sortOrder)
    {
        String jsonResponse = HttpRequester.send(UriBuilder.buildForMovies(sortOrder));

        JSONObject jsonObj = toJsonObject(jsonResponse);
        if (jsonObj == null)
        {
            return new Hashtable<>(0);
        }

        return parseMovies(jsonObj);
    }

    public static Trailer[] fetchTrailersFor(long movieId)
    {
        String jsonResponse = HttpRequester.send(UriBuilder.buildForTrailers(movieId));

        JSONObject jsonObj = toJsonObject(jsonResponse);
        if (jsonObj == null)
        {
            return new Trailer[0];
        }

        return parseTrailers(jsonObj);
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

    private static Trailer[] parseTrailers(JSONObject jsonObj)
    {
        ArrayList<Trailer> trailers = new ArrayList<>();
        try
        {
            JSONArray results = jsonObj.getJSONArray(TrailerJsonTags.TAG_RESULTS);
            for (int i = 0; i < results.length(); i++)
            {
                JSONObject trailerObj = results.getJSONObject(i);

                final String trailerKey  = trailerObj.getString(TrailerJsonTags.TAG_KEY);
                final String trailerSite = trailerObj.getString(TrailerJsonTags.TAG_SITE);

                if (!trailerSite.equalsIgnoreCase(TrailerJsonTags.EXPECTED_SITE_VALUE))
                {
                    Log.w(LOG_TAG, String.format("Expected the trailer site to be %s, but was %s", TrailerJsonTags.EXPECTED_SITE_VALUE, trailerSite));
                    continue;
                }

                trailers.add(new Trailer(
                    i,
                    trailerKey,
                    String.format(
                        App.getContext().getResources().getString(R.string.trailer_title_format),
                        String.valueOf(i + 1)
                    ))
                );
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error with JSON object");
        }

        return trailers.toArray(new Trailer[trailers.size()]);
    }
}
