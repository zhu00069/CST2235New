package com.example.zhubo.androidlabsnew;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class WeatherForecastActivity extends Activity {
    private ImageView img_weather;
    private TextView text_crtTemp;
    private TextView text_minTemp;
    private TextView text_maxTemp;
    private TextView text_wind;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        img_weather = findViewById(R.id.weatherImage);
        text_crtTemp = findViewById(R.id.crtTemp);
        text_minTemp = findViewById(R.id.minTemp);
        text_maxTemp = findViewById(R.id.maxTemp);
        text_wind = findViewById(R.id.wind);
        progressBar = findViewById(R.id.progress);

        //set progress bar to be visible
        progressBar.setVisibility(View.VISIBLE);
        //start a thread to parse xml information
        ForecastQuery forecast = new ForecastQuery();
        forecast.execute();
    }
        public class ForecastQuery extends AsyncTask<String, Integer, String> {
            String windSpeed;
            String minTemp;
            String maxTemp;
            String crtTemp;
            String weatherName;
            Bitmap crtWeather;


            protected String doInBackground(String... args){
                 //From: Parsing XML Data.[Web Page] Retrieved from: https://developer.android.com/training/basics/network-ops/xml.html
                // Given a string representation of a URL, sets up a connection and gets an input stream.
                URL url;
                InputStream inStream = null;
                try {
                    //conncet to an URL, and parser the xml info
                    url = new URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");
                    HttpURLConnection conn;
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    // Starts the query
                    conn.connect();
                    inStream = conn.getInputStream();

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser parser = factory.newPullParser();
                    parser.setInput(inStream, null);
                    int eventType = parser.getEventType();

                    //while not end of document
                    while(eventType != XmlPullParser.END_DOCUMENT){
                        //check eventType
                        switch(eventType){
                            //if eventType is START_TAG, parser the info needed
                            case XmlPullParser.START_TAG:
                                String tagname = parser.getName();
                                //if tag name is "temperature", get the current temperature, min temperatur, max temperature
                                if (tagname.equalsIgnoreCase("temperature")) {
                                    crtTemp = parser.getAttributeValue(null, "value");
                                    publishProgress(25);
                                    minTemp = parser.getAttributeValue(null, "min");
                                    publishProgress(50);
                                    maxTemp = parser.getAttributeValue(null, "max");
                                    publishProgress(75);
                                    //if tag name is "wind", get the wind speed
                                }else if(parser.getName().equalsIgnoreCase("speed")){
                                    windSpeed = parser.getAttributeValue(null, "value");
                                    //if tag name is "weather", get the image name
                                }else if (parser.getName().equalsIgnoreCase("weather")){
                                    //parser the image name
                                    weatherName = parser.getAttributeValue(null, "icon");

                                    //if image file exists, read it
                                    String imagefile = weatherName + ".png";
                                    if(fileExistance(imagefile)){
                                        FileInputStream fis = null;
                                        try {
                                            fis = openFileInput(imagefile);
                                        }catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                        crtWeather = BitmapFactory.decodeStream(fis);
                                        Log.i(imagefile, " image is found locally.");
                                        //else download it and save it
                                    }else{
                                        //get the url of the image
                                        url = new URL("http://openweathermap.org/img/w/" + imagefile);
                                        //download the image from website
                                        crtWeather = HttpUtils.getImage(url);
                                        FileOutputStream outputStream = openFileOutput( imagefile, Context.MODE_PRIVATE);
                                        crtWeather.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                        outputStream.flush();
                                        outputStream.close();
                                        Log.i(imagefile, " image has been downloaded.");
                                    }
                                    publishProgress(100);
                                }
                                break;
                            default:
                                break;
                        }
                        eventType = parser.next();
                    }
                } catch (XmlPullParserException | IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        inStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            //check if image file exists
            boolean fileExistance(String fname){
                File file = getBaseContext().getFileStreamPath(fname);
                return file.exists();
            }

            //each time publishprogress() is called, show the progress of GUI
            @Override
            protected void onProgressUpdate(Integer... values) {
                //set progress bar to be visible
                progressBar.setVisibility(View.VISIBLE);
                //set the progress bar
                progressBar.setProgress(values[0]);
                super.onProgressUpdate(values);
            }

            //get the info from doInBackground(), and update the info on GUI
            @Override
            protected void onPostExecute(String s) {
                if(crtTemp != null) {
                    text_crtTemp.setText(getString(R.string.crtTemp) + crtTemp);
                }
                if(minTemp != null) {
                    text_minTemp.setText(getString(R.string.minTemp) + minTemp);
                }
                if(maxTemp != null) {
                    text_maxTemp.setText(getString(R.string.maxTemp) + maxTemp);
                }
                if(crtWeather != null) {
                    img_weather.setImageBitmap(crtWeather);
                }

                if(windSpeed != null) {
                    text_wind.setText(getString(R.string.wind) + windSpeed);
                }
                super.onPostExecute(s);
                //set progress bar to be invisible
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
}