package com.inspius.yo_video.helper;

/**
 * Created by Billy on 8/1/16.
 */
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

// Source: https://stackoverflow.com/questions/12910503/android-read-file-as-string
public class FileUtil {

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    public static String getStringFromFile(File file) throws Exception {
        FileInputStream fin = new FileInputStream(file);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }

    public static byte[] convertFileToByteArray(File file) throws Exception {
        FileInputStream fin = new FileInputStream(file);

        byte[] buffer = new byte[1024];
        ByteArrayOutputStream byteStream =
                new ByteArrayOutputStream(fin.available());
        int count;

        while (true) {
            count = fin.read(buffer);
            if (count <= 0)
                break;
            byteStream.write(buffer, 0, count);
        }

        fin.close();
        return null;
    }
}
