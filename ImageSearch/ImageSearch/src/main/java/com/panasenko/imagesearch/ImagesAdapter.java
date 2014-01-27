/**
 * File: ImagesAdapter.java
 * Created: 1/26/14
 * Author: Viacheslav Panasenko
 */
package com.panasenko.imagesearch;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * ImagesAdapter
 * Class description
 */
public class ImagesAdapter extends BaseAdapter {

    private Context context;
    private List<String> data;
    private ImageLoader imgLoader;

    public ImagesAdapter(Context ctx, List<String> imageUrls) {
        context = ctx;
        data = imageUrls;
        imgLoader = ImageLoader.getInstance();
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
            view = new ImageView(context);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }

        String url = getItem(i).toString();
        imgLoader.displayImage(url, (ImageView) view);
        return view;
    }
}
