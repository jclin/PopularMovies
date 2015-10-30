package com.jclin.popularmovies.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.jclin.popularmovies.R;
import com.jclin.popularmovies.activities.MovieDetailsActivity;
import com.jclin.popularmovies.activities.MovieReviewsActivity;
import com.jclin.popularmovies.adapters.TrailerAdapter;
import com.jclin.popularmovies.contentProviders.ReviewsContract;
import com.jclin.popularmovies.contentProviders.TrailersContract;
import com.jclin.popularmovies.data.FavoriteMovieQuery;
import com.jclin.popularmovies.data.ImageProvider;
import com.jclin.popularmovies.data.ImageSize;
import com.jclin.popularmovies.data.Movie;
import com.jclin.popularmovies.data.TrailerShareIntent;
import com.jclin.popularmovies.data.UriBuilder;
import com.jclin.popularmovies.loaders.LoaderFactory;
import com.jclin.popularmovies.loaders.LoaderIDs;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class MovieDetailsActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final String LOG_TAG     = MovieDetailsActivity.class.getName();
    private static final String MOVIE_KEY   = "MOVIE_BUNDLE_KEY";

    protected @Bind(R.id.textView_movie_title)         TextView _movieTitleTextView;
    protected @Bind(R.id.textView_release_date)        TextView _releaseDateTextView;
    protected @Bind(R.id.textView_user_rating)         TextView _userRatingTextView;
    protected @Bind(R.id.textView_plot_synopsis)       TextView _plotSynopsisTextView;
    protected @Bind(R.id.switch_favorite)              Switch   _switchFavorite;
    protected @Bind(R.id.textView_reviews)             TextView _reviewsTextView;

    protected @Bind(R.id.list_view_trailers)           ListView _trailersListView;
    protected @Bind(R.id.progressBar_trailers_loading) ProgressBar _loadingTrailersProgressBar;
    protected @Bind(R.id.textView_no_trailers)         TextView _noTrailersTextView;

    protected @Bind(R.id.root_linear_layout) LinearLayout _rootLinearLayout;

    private MenuItem _shareMenuItem;
    private ShareActionProvider _shareActionProvider;

    private Movie _movie;
    private TrailerAdapter _trailerAdapter;

    public MovieDetailsActivityFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.fragment_movie_summary, menu);

        _shareMenuItem = menu.findItem(R.id.menu_item_share_trailer);
        _shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(_shareMenuItem);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        _trailersListView = createTrailersListView(inflater, container);
        ButterKnife.bind(this, _trailersListView);

        _movie = getMovieFromIntent(savedInstanceState);

        _trailerAdapter = new TrailerAdapter(getActivity(), null, 0);
        _trailersListView.setAdapter(_trailerAdapter);

        initTrailersLoader(_movie);

        populateControls(_movie);

        _rootLinearLayout
            .getViewTreeObserver()
            .addOnGlobalLayoutListener(new ImageViewHeightAdjuster(_rootLinearLayout, _movie));

        return _trailersListView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putParcelable(MOVIE_KEY, _movie);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        return LoaderFactory.createFor(LoaderIDs.parse(id), getActivity(), args);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        _trailerAdapter.swapCursor(data);

        hideLoadingProgress(_trailerAdapter.getCount());

        updateShareActionProvider(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        _trailerAdapter.swapCursor(null);
    }

    @OnItemClick(R.id.list_view_trailers)
    protected void onListViewItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Uri youTubeTrailerUri = Uri.parse((String) view.getTag());
        Intent viewTrailerIntent = new Intent(Intent.ACTION_VIEW, youTubeTrailerUri);

        startActivity(viewTrailerIntent);
    }

    @OnClick(R.id.textView_reviews)
    protected void onReviewsTextViewClick()
    {
        Intent reviewsIntent = new Intent(getActivity(), MovieReviewsActivity.class);
        reviewsIntent.putExtra(ReviewsContract.MOVIE_ID_BUNDLE_KEY, _movie.getId());

        startActivity(reviewsIntent);
    }

    @OnCheckedChanged(R.id.switch_favorite)
    protected void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        if (isChecked)
        {
            FavoriteMovieQuery.insert(_movie, getActivity().getContentResolver());
        }
        else
        {
            FavoriteMovieQuery.remove(_movie, getActivity().getContentResolver());
        }
    }

    private Movie getMovieFromIntent(Bundle savedInstanceState)
    {
        if (savedInstanceState != null)
        {
            Log.i(LOG_TAG, "Restoring Movie instance...");
            _movie = savedInstanceState.getParcelable(MOVIE_KEY);
        }

        return getActivity()
            .getIntent()
            .getParcelableExtra(getString(R.string.INTENT_DATA_MOVIE));
    }

    private void populateControls(Movie movie)
    {
        _movieTitleTextView.setText(movie.getOriginalTitle());
        _releaseDateTextView.setText(movie.getReleaseDateString());
        _userRatingTextView.setText(String.format("%.1f", movie.getVoteAverage()));
        _plotSynopsisTextView.setText(movie.getOverview());
        _switchFavorite.setChecked(FavoriteMovieQuery.alreadyExists(movie, getActivity().getContentResolver()));

        _reviewsTextView.setText(Html.fromHtml(getString(R.string.reviews_html_string)));
    }

    private void initTrailersLoader(Movie movie)
    {
        showLoadingProgress();

        Bundle trailersLoaderArgs = new Bundle();
        trailersLoaderArgs.putLong(TrailersContract.MOVIE_ID_BUNDLE_KEY, movie.getId());

        getActivity().getSupportLoaderManager().initLoader(
            LoaderIDs.MovieTrailers.id(),
            trailersLoaderArgs,
            this
            );
    }

    @NonNull
    private ListView createTrailersListView(LayoutInflater inflater, ViewGroup container)
    {
        ListView listView = (ListView) inflater.inflate(
            R.layout.frament_movie_trailers_listview,
            container,
            false
            );

        listView.addHeaderView(inflater.inflate(
            R.layout.fragment_movie_summary,
            container)
            );

        return listView;
    }

    private void showLoadingProgress()
    {
        _loadingTrailersProgressBar.setVisibility(View.VISIBLE);
        _rootLinearLayout.setVisibility(View.INVISIBLE);
        _noTrailersTextView.setVisibility(View.GONE);
    }

    private void hideLoadingProgress(int adapterItemCount)
    {
        _loadingTrailersProgressBar.setVisibility(View.GONE);
        _rootLinearLayout.setVisibility(View.VISIBLE);

        if (adapterItemCount == 0)
        {
            _noTrailersTextView.setVisibility(View.VISIBLE);
        }
    }

    private void updateShareActionProvider(Cursor trailerCursor)
    {
        if (!trailerCursor.moveToFirst())
        {
            _shareMenuItem.setVisible(false);
            return;
        }

        Intent trailerShareIntent = TrailerShareIntent.buildWith(
            _movie.getOriginalTitle(),
            trailerCursor.getString(TrailersContract.Columns.YouTubeUrl.getIndex())
            );

        _shareActionProvider.setShareIntent(trailerShareIntent);
    }

    private final class ImageViewHeightAdjuster implements ViewTreeObserver.OnGlobalLayoutListener
    {
        private final LinearLayout _rootLinearLayout;
        private final Movie _movie;

        public ImageViewHeightAdjuster(LinearLayout rootLinearLayout, Movie movie)
        {
            _rootLinearLayout = rootLinearLayout;
            _movie            = movie;
        }

        @Override
        public void onGlobalLayout()
        {
            ImageView imageView = (ImageView)_rootLinearLayout.findViewById(R.id.imageView_movie_poster);

            int pixelWidth = getActivity()
                    .getResources()
                    .getDimensionPixelSize(R.dimen.image_movie_poster_width);

            int pixelHeight = ImageSize.pixelHeightFrom(pixelWidth);

            if (imageView.getLayoutParams().height == pixelHeight)
            {
                return;
            }

            imageView.setLayoutParams(new LinearLayout.LayoutParams(pixelWidth, pixelHeight));

            ImageProvider.beginLoadFor(
                    UriBuilder.buildForImage(_movie.getPosterPath()),
                    pixelWidth,
                    pixelHeight,
                    imageView
            );
        }
    }
}
