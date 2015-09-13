package com.jclin.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.jclin.popularmovies.App;
import com.jclin.popularmovies.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class Movie implements Parcelable
{
    private static final String DATE_FORMAT          = "yyyy-MM-dd";
    private static final long UNKNOWN_RELEASE_DATE   = Long.MIN_VALUE;

    private final long   _id;
    private final String _originalTitle;
    private final String _overview;
    private final String _posterPath;
    private final double _voteAverage;
    private final long _releaseTimeMilliseconds;

    public static final Parcelable.Creator<Movie> CREATOR = new MovieParcelCreator();

    public long getId()
    {
        return _id;
    }

    public String getOriginalTitle()
    {
        return _originalTitle.equals("") ?
            App.getContext().getString(R.string.no_movie_title) :
            _originalTitle;
    }

    public String getOverview()
    {
        return _overview.equals("") ?
            App.getContext().getString(R.string.no_movie_plot_synopsis) :
            _overview;
    }

    public String getPosterPath()
    {
        return _posterPath;
    }

    public double getVoteAverage()
    {
        return _voteAverage;
    }

    public String getReleaseDateString()
    {
        return _releaseTimeMilliseconds == UNKNOWN_RELEASE_DATE ?
            App.getContext().getString(R.string.unknown_release_date) :
            new SimpleDateFormat(DATE_FORMAT, Locale.US).format(new Date(_releaseTimeMilliseconds));
    }

    public Movie(
        long id,
        String originalTitle,
        String overview,
        String posterPath,
        double voteAverage,
        Date releaseDate
        )
    {
        _id             = id;
        _originalTitle  = originalTitle;
        _overview       = overview;
        _posterPath     = posterPath;
        _voteAverage    = voteAverage;

        _releaseTimeMilliseconds = releaseDate != null ?
            releaseDate.getTime() :
            UNKNOWN_RELEASE_DATE;
    }

    private Movie(Parcel source)
    {
        _id                      = source.readLong();
        _originalTitle           = source.readString();
        _overview                = source.readString();
        _posterPath              = source.readString();
        _voteAverage             = source.readDouble();
        _releaseTimeMilliseconds = source.readLong();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeLong(_id);
        dest.writeString(_originalTitle);
        dest.writeString(_overview);
        dest.writeString(_posterPath);
        dest.writeDouble(_voteAverage);
        dest.writeLong(_releaseTimeMilliseconds);
    }

    private static final class MovieParcelCreator implements Parcelable.Creator<Movie>
    {
        @Override
        public Movie createFromParcel(Parcel source)
        {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size)
        {
            return new Movie[size];
        }
    }
}
