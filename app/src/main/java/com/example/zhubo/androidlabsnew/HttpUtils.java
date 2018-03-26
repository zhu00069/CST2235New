package com.example.zhubo.androidlabsnew;

/**
 * Created by zhubo on 2018-03-20.
 */

//From: Get Bitmap from Url with HttpURLConnection : Bitmap « 2D Graphics « Android [Web Page].
//Retrieved from:http://www.java2s.com/Code/Android/2D-Graphics/GetBitmapfromUrlwithHttpURLConnection.htm
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author Terry E-mail: yaoxinghuo at 126 dot com
 * @version create: 2010-10-21 ??01:40:03
 */
public class HttpUtils {
    public static Bitmap getImage(URL url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
//            BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
//            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//            //We create an array of bytes
//            byte[] data = new byte[50];
//            int current = 0;
//
//            while((current = bis.read(data,0,data.length)) != -1){
//                buffer.write(data,0,current);
//            }
//
//            return BitmapFactory.decodeByteArray(buffer.toByteArray(), 0, buffer.size());
            return BitmapFactory.decodeStream(connection.getInputStream());
        } catch (Exception e) {
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    public static Bitmap getImage(String urlString) {
        try {
            URL url = new URL(urlString);
            return getImage(url);
        } catch (MalformedURLException e) {
            return null;
        }
    }

}

