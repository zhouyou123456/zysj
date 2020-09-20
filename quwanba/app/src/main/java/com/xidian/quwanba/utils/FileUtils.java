package com.xidian.quwanba.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.URL;

public class FileUtils {

    public static Bitmap getImage(String urlStr) {
        Bitmap bitmap = null;
        try {

            URL url = new URL(urlStr);
            InputStream is = url.openStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block

        }
        return bitmap;
    }



}
