package com.jclin.popularmovies.data;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public final class GetMoviesTask extends AsyncTask<Void, Integer, Movie[]>
{
    private static final String LOG_TAG = GetMoviesTask.class.getName();

    private final SortOrder _sortOrder;

    private OnMoviesRetrievedListener _moviesRetrievedListener;

    public GetMoviesTask(SortOrder sortOrder)
    {
        _sortOrder = sortOrder;
    }

    protected Movie[] doInBackground(Void... params)
    {
        String jsonResponse = HttpRequester.send(TheMovieDBUri.buildForMovies(_sortOrder));

        JSONObject jsonObj = toJsonObject(jsonResponse);
        if (jsonObj == null)
        {
            return null;
        }

        return parseMovies(jsonObj);
    }

    @Override
    protected void onPostExecute(Movie[] movies)
    {
        if (_moviesRetrievedListener != null)
        {
            _moviesRetrievedListener.onMoviesRetrieved(movies);
        }
    }

    private JSONObject toJsonObject(String rawJson)
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

    private Movie[] parseMovies(JSONObject jsonObj)
    {
        ArrayList<Movie> movies = new ArrayList<>();
        try
        {
            JSONArray results = jsonObj.getJSONArray(MovieJsonTags.TAG_RESULTS);
            for (int i = 0; i < results.length(); i++)
            {
                JSONObject movieObj = results.getJSONObject(i);

                movies.add(new Movie(
                    movieObj.getLong(MovieJsonTags.TAG_ID),
                    ValidatedJson.parseString(movieObj, MovieJsonTags.TAG_ORIGINAL_TITLE),
                    ValidatedJson.parseString(movieObj, MovieJsonTags.TAG_OVERVIEW),
                    ValidatedJson.parseString(movieObj, MovieJsonTags.TAG_POSTER_PATH).replace("/", ""),
                    movieObj.getDouble(MovieJsonTags.TAG_VOTE_AVERAGE),
                    ValidatedJson.parseDate(movieObj, MovieJsonTags.TAG_RELEASE_DATE)
                    )
                );
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error with JSON object", e);
        }

        return movies.toArray(new Movie[movies.size()]);
    }

    public interface OnMoviesRetrievedListener
    {
        void onMoviesRetrieved(Movie[] movies);
    }

    public void setOnMoviesRetrievedListener(OnMoviesRetrievedListener listener)
    {
        _moviesRetrievedListener = listener;
    }
}
