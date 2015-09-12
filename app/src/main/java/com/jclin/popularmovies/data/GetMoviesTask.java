package com.jclin.popularmovies.data;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.jclin.popularmovies.BuildConfig;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        String jsonResponse = HttpRequester.send(buildRequest(_sortOrder));

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

    @NonNull
    private Uri buildRequest(SortOrder sortOrder)
    {
        return new Uri.Builder()
            .scheme("http")
            .authority("api.themoviedb.org")
            .appendPath("3")
            .appendPath("discover")
            .appendPath("movie")
            .appendQueryParameter("sort_by", sortOrder.getQueryParam())
            .appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY)
            .build();
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
            JSONArray results = jsonObj.getJSONArray(MovieConsts.TAG_RESULTS);
            for (int i = 0; i < results.length(); i++)
            {
                JSONObject movieObj = results.getJSONObject(i);

                movies.add(new Movie(
                    movieObj.getLong(MovieConsts.TAG_ID),
                    movieObj.getString(MovieConsts.TAG_ORIGINAL_TITLE),
                    movieObj.getString(MovieConsts.TAG_OVERVIEW),
                    movieObj.getString(MovieConsts.TAG_POSTER_PATH).replace("/", ""),
                    movieObj.getDouble(MovieConsts.TAG_VOTE_AVERAGE),
                    new SimpleDateFormat(MovieConsts.DATE_FORMAT).parse(movieObj.getString(MovieConsts.TAG_RELEASE_DATE))
                    )
                );
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error with JSON object", e);
        }
        catch (ParseException pe)
        {
            pe.printStackTrace();
            Log.e(LOG_TAG, "Error parsing JSON object", pe);
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
