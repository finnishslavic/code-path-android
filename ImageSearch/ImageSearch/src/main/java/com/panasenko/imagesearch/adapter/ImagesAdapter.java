/**
 * File: ImagesAdapter.java
 * Created: 1/26/14
 * Author: Viacheslav Panasenko
 */
package com.panasenko.imagesearch.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.panasenko.imagesearch.R;

import java.util.List;

/**
 * ImagesAdapter
 * Class description
 */
public class ImagesAdapter extends BaseAdapter {

    private Context context;
    private List<String> data;
    private DisplayImageOptions options;
    private LayoutInflater inflater;

    public ImagesAdapter(Context ctx, List<String> imageUrls) {
        context = ctx;
        data = imageUrls;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (data == null) {
            throw new IllegalStateException("Adapter was not initialized with data");
        }

        return data.size();
    }

    @Override
    public Object getItem(int i) {
        if (data == null || i >= data.size()) {
            throw new IndexOutOfBoundsException("Requested non existen item: " + i);
        }

        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.grid_item, viewGroup, false);
        }

        String url = getItem(i).toString();
        ImageLoader.getInstance().displayImage(url, (ImageView) view.findViewById(R.id.img_result),
                options);
        return view;
    }
}
