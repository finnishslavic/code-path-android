/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.panasenko.codepath.todoapp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FileUtils
 * Helper class for easy read/write file procedures.
 */
public class FileUtils
{
    public static void writeLines(File destination, List<String> source) throws IOException {
        if (destination != null && source != null && !source.isEmpty()) {
            destination.createNewFile();
            PrintWriter writer = new PrintWriter(new FileWriter(destination));
            for (String line:source) {
                writer.println(line);
            }

            writer.close();
        }
    }

    public static List<String> readLines(File source) throws IOException {
        List<String> strings = new ArrayList<String>();
        if (source != null && source.canRead()) {
            BufferedReader reader = new BufferedReader(new FileReader(source));
            String tmpLine;
            while ((tmpLine = reader.readLine()) != null) {
                strings.add(tmpLine);
            }

            reader.close();
        }

        return strings;
    }
}
