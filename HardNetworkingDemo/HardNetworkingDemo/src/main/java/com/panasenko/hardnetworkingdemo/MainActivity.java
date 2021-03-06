package com.panasenko.hardnetworkingdemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends Activity {

    ImageView ivBasicImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivBasicImage = (ImageView) findViewById(R.id.iv_basic_image);
        String imageUrl
                = "http://2.gravatar.com/avatar/858dfac47ab8176458c005414d3f0c36?s=128&d=&r=G";
        new ImageDownloadTask().execute(imageUrl);
    }

    private class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... addresses) {
            // Convert string to URL
            URL url = getUrlFromString(addresses[0]);
            // Get input stream
            InputStream in = getInputStream(url);
            // Decode bitmap
            Bitmap bitmap = decodeBitmap(in);
            // Return bitmap result
            return bitmap;
        }

        private URL getUrlFromString(String address) {
            URL url;
            try {
                url = new URL(address);
            } catch (MalformedURLException e1) {
                url = null;
            }
            return url;
        }

        private InputStream getInputStream(URL url) {
            InputStream in;
            // Open connection
            URLConnection conn;
            try {
                conn = url.openConnection();
                conn.connect();
                in = conn.getInputStream();
            } catch (IOException e) {
                in = null;
            }
            return in;
        }

        private Bitmap decodeBitmap(InputStream in) {
            Bitmap bitmap;
            try {
                // Turn response into Bitmap
                bitmap = BitmapFactory.decodeStream(in);
                // Close the input stream
                in.close();
            } catch (IOException e) {
                in = null;
                bitmap = null;
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // Set bitmap image for the result
            ivBasicImage.setImageBitmap(result);
        }
    }
}