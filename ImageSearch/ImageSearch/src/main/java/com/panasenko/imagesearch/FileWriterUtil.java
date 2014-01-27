/**
 * File: FileWriterUtil.java
 * Created: 1/25/14
 * Author: Viacheslav Panasenko
 */
package com.panasenko.imagesearch;

import android.content.Context;
import android.util.Log;

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
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
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
                    ctx.getCacheDir().getAbsoluteFile() + File.pathSeparator + FILE_NAME));
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

}
