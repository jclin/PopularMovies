package com.jclin.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public final class Movie implements Parcelable
{
    private final long   _id;
    private final String _originalTitle;
    private final String _overview;
    private final String _posterPath;
    private final double _voteAverage;
    private final Date   _releaseDate;

    public static final Parcelable.Creator<Movie> CREATOR = new MovieParcelCreator();

    public long getId()
    {
        return _id;
    }

    public String getOriginalTitle()
    {
        return _originalTitle;
    }

    public String getOverview()
    {
        return _overview;
    }

    public String getPosterPath()
    {
        return _posterPath;
    }

    public double getVoteAverage()
    {
        return _voteAverage;
    }

    public Date getRelaseDate()
    {
        return _releaseDate;
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
        _releaseDate    = releaseDate;
    }

    private Movie(Parcel source)
    {
        _id            = source.readLong();
        _originalTitle = source.readString();
        _overview      = source.readString();
        _posterPath    = source.readString();
        _voteAverage   = source.readDouble();
        _releaseDate    = new Date(source.readLong());
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
        dest.writeLong(_releaseDate.getTime());
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
