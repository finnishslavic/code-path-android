/**
 * File: FileWriterUtil.java
 * Created: 1/25/14
 * Author: Viacheslav Panasenko
 */
package com.panasenko.imagesearch.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.panasenko.imagesearch.R;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FileWriterUtil
 * File writing utilities.
 */
public class FileWriterUtil {

    private static final String TAG = FileWriterUtil.class.getSimpleName();

    private static final String FILE_NAME = "temp_results.txt";

    /**
     * Writes given list line by line to a temp file.
     * @param ctx Parent context to access cache folder.
     * @param strings List of string to be stored in a file.
     */
    public static void writeStringsToFile(Context ctx, List<String> strings) {
        File outputFile = new File(ctx.getCacheDir(), FILE_NAME);
        if (!outputFile.exists()) {
            try {
                outputFile.createNewFile();
            } catch (IOException e) {
                Log.e(TAG, "Error writing to file");
            }
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, true));

            for (String str:strings) {
                writer.write(str + "\n");
            }
            writer.close();
        } catch (IOException e) {
            Log.e(TAG, "Error accessing file", e);
        }
    }

    /**
     * Reads strings from a temp file to a List.
     * @param ctx Parent context to access cache folder.
     * @return List of strings read from temp file.
     */
    public static List<String> readStringsFromFile(Context ctx) {
        List<String> result = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(
                    ctx.getCacheDir().getAbsoluteFile() + File.separator + FILE_NAME));
            result = new ArrayList<String>();
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Cleans up temporary cache files.
     * @param ctx Parent context, used to access internal file storage.
     */
    public static void cleanCache(Context ctx) {
        ctx.getCacheDir().delete();
    }

    /**
     * Returns stream uri from given view with ImageView.
     * @param parent View that has ImageView inside.
     * @return Uri for the image stream used for sharing.
     */
    public static Uri getStreamUri(View parent) {
        if (parent == null || parent.findViewById(R.id.img_result) == null) {
            return null;
        }

        ImageView img = (ImageView) parent.findViewById(R.id.img_result);
        Bitmap bmp = ((BitmapDrawable) img.getDrawable()).getBitmap();
        Uri bmpUri = null;
        try {
            File file =  new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "share_image.png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            Log.e(TAG, "Failed to save file", e);
        }

        return bmpUri;
    }

}
