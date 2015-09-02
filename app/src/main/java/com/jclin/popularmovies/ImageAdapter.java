package com.jclin.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public final class ImageAdapter extends BaseAdapter
{
    private final Context _context;
    private final Uri[] _posterImageUris;

    private int _numColumns      = 0;
    private int _itemPixelWidth  = 0;
    private int _itemPixelHeight = 0;

    private AbsListView.LayoutParams _imageViewLayoutParams;

    public int getNumColumns()
    {
        return _numColumns;
    }

    public void setNumColumns(int numColumns)
    {
        _numColumns = numColumns;
    }

    public ImageAdapter(Context context)
    {
        _context = context;

        ArrayList<Uri> posterImageUris = new ArrayList<>();
        posterImageUris.add(buildPosterImageUri("kqjL17yufvn9OVLyXYpvtyrFfak.jpg"));
        posterImageUris.add(buildPosterImageUri("s5uMY8ooGRZOL0oe4sIvnlTsYQO.jpg"));
        posterImageUris.add(buildPosterImageUri("7SGGUiTE6oc2fh9MjIk5M00dsQd.jpg"));
        posterImageUris.add(buildPosterImageUri("uXZYawqUsChGSj54wcuBtEdUJbh.jpg"));
        posterImageUris.add(buildPosterImageUri("6iQ4CMtYorKFfAmXEpAQZMnA0Qe.jpg"));
        posterImageUris.add(buildPosterImageUri("xxOKDTQbQo7h1h7TyrQIW7u8KcJ.jpg"));
        posterImageUris.add(buildPosterImageUri("5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg"));
        posterImageUris.add(buildPosterImageUri("t90Y3G8UGQp0f0DrP60wRu9gfrH.jpg"));
        posterImageUris.add(buildPosterImageUri("yUlpRbbrac0GTNHZ1l20IHEcWAN.jpg"));
        posterImageUris.add(buildPosterImageUri("aBBQSC8ZECGn6Wh92gKDOakSC8p.jpg"));
        posterImageUris.add(buildPosterImageUri("qFC07nj9lWWmnbkS191AgFUth9J.jpg"));
        posterImageUris.add(buildPosterImageUri("3zQvuSAUdC3mrx9vnSEpkFX0968.jpg"));

        _posterImageUris = posterImageUris.toArray(new Uri[posterImageUris.size()]);
    }

    @Override
    public int getCount()
    {
        return 12;
    }

    @Override
    public Object getItem(int position)
    {
        return R.drawable.minions;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = ((LayoutInflater) _context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.gridview_item_imageview, parent, false);
        }

        ImageView imageView = (ImageView) convertView;

        if (_imageViewLayoutParams == null)
        {
            return imageView;
        }

        imageView.setLayoutParams(_imageViewLayoutParams);

        if (imageView.getLayoutParams().height != _itemPixelHeight)
        {
            imageView.setLayoutParams(_imageViewLayoutParams);
        }

        Picasso
            .with(_context)
            .load(_posterImageUris[position])
            .resize(_itemPixelWidth, _itemPixelHeight)
            .centerInside()
            .error(R.drawable.minions)
            .into(imageView);

        return imageView;
    }

    public void setItemPixelWidth(int itemPixelWidth)
    {
        if (_itemPixelWidth == itemPixelWidth)
        {
            return;
        }

        _itemPixelWidth  = itemPixelWidth;
        _itemPixelHeight = calculateItemPixelHeightFrom(itemPixelWidth);

        _imageViewLayoutParams = new GridView.LayoutParams(_itemPixelWidth, _itemPixelHeight);

        notifyDataSetChanged();
    }

    private static Uri buildPosterImageUri(String relativePath)
    {
        final String imageSizePath = "w185";

        return new Uri.Builder()
                .scheme("http")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath(imageSizePath)
                .appendPath(relativePath)
                .build();
    }

    private int calculateItemPixelHeightFrom(int itemPixelWidth)
    {
        return (itemPixelWidth *_context.getResources().getInteger(R.integer.image_thumbnail_pixel_height)) /
                _context.getResources().getInteger(R.integer.image_thumbnail_pixel_width);
    }
}
