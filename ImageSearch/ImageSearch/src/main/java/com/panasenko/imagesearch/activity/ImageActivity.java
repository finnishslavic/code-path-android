/**
 * File: ImageActivity.java
 * Created: 1/28/14
 * Author: Viacheslav Panasenko
 */
package com.panasenko.imagesearch.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.panasenko.imagesearch.R;
import com.panasenko.imagesearch.util.FileWriterUtil;

/**
 * ImageActivity
 * Shows a full screen image view.
 */
public class ImageActivity extends Activity {

    public static final String EXTRA_URL = "com.panasenko.imagesearch.EXTRA_URL";

    private ImageView imageView;
    private String url;
    private ShareActionProvider shareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_item);

        imageView = (ImageView) findViewById(R.id.img_result);
        if (getIntent().hasExtra(EXTRA_URL)) {
            url = getIntent().getStringExtra(EXTRA_URL);
            showImage();
        } else {
            // No image passed :(
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRA_URL, url);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        url = savedInstanceState.getString(EXTRA_URL);
        showImage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);
        // Fetch and store ShareActionProvider
        shareActionProvider = (ShareActionProvider) item.getActionProvider();

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");

        Uri uri = FileWriterUtil
                .getStreamUri(ImageActivity.this.findViewById(android.R.id.content));
        if (uri != null) {
            share.putExtra(Intent.EXTRA_STREAM, uri);
        }

        shareActionProvider.setShareIntent(share);

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Shows image for url.
     */
    private void showImage() {
        ImageLoader.getInstance().displayImage(url, imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                // Do nothing
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                finish();
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                invalidateOptionsMenu();
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }


}
