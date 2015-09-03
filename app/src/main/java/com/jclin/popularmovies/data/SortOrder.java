package com.jclin.popularmovies.data;

public enum SortOrder
{
    Popularity
    {
        @Override
        public String getQueryParam()
        {
            return "popularity.desc";
        }
    },

    Rating
    {
        @Override
        public String getQueryParam()
        {
            return "vote_average.desc";
        }
    };

    public abstract String getQueryParam();
}
