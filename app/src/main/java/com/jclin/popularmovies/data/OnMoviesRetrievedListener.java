package com.jclin.popularmovies.data;

public interface OnMoviesRetrievedListener
{
    void onMoviesRetrieved(Movie[] movies, SortOrder sortedBy);
}
