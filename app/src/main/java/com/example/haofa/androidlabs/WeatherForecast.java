package com.example.haofa.androidlabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {

    private TextView tempText;
    private TextView minText;
    private TextView maxText;
    private ImageView weatherImage;
    private ProgressBar loadingimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        loadingimage = (ProgressBar) findViewById(R.id.progressBar);
        loadingimage.setVisibility(View.VISIBLE);

        tempText = (TextView)findViewById(R.id.tempCurrent) ;
        minText = (TextView)findViewById(R.id.tempMin) ;
        maxText = (TextView)findViewById(R.id.tempMax) ;
        weatherImage = (ImageView)findViewById(R.id.imageView);
        ForecastQuery fq = new ForecastQuery();
        fq.execute();


    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        private String temp;
        private String minT;
        private String maxT;
        private String icon;
        private Bitmap bmtep;

        @Override
        protected String doInBackground(String... args) {
            URL url = null;
            InputStream input = null;
            String ns = null;
            HttpURLConnection conn = null;


            try {
                url = new URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");
                conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
            /*return conn.getInputStream();*/
                input = conn.getInputStream();

                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(input, null);
                parser.nextTag();
                parser.require(XmlPullParser.START_TAG, null, "current");

                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String name = parser.getName();
                    // Starts by looking for the entry tag
                    if (name.equals("temperature")) {
                        temp = parser.getAttributeValue(null, "value") + " C";
                        publishProgress(25);
                        minT = parser.getAttributeValue(null, "min") + " C";
                        publishProgress(50);
                        maxT = parser.getAttributeValue(null, "max") + " C";
                        publishProgress(75);

                    } else if(name.equals("weather")){
                        icon = parser.getAttributeValue(null, "icon");
                        String filename = icon + ".png";

                        if(fileExistance(filename)) {
                            FileInputStream fis = null;
                            fis = openFileInput(filename);
                            bmtep = BitmapFactory.decodeStream(fis);
                            Log.i("Existed:","Find a local image");

                        }else {
                            String urlString = "http://openweathermap.org/img/w/" + icon;
                            bmtep = getImage(urlString);
                            SaveImage(bmtep, icon);
                            Log.i("Non-Existed:","Download from website");
                        }
                        publishProgress(100);

                    }
                    else
                        skip(parser);


                }


            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return ns;
        }
        @Override
        protected void onPostExecute(String s) {

            tempText.setText(temp);
            tempText.setTextSize(20);
            minText.setText(minT);
            minText.setTextSize(20);
            maxText.setText(maxT);
            maxText.setTextSize(20);
            weatherImage.setImageBitmap(bmtep);
            loadingimage.setVisibility(View.INVISIBLE);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            loadingimage.setVisibility(View.VISIBLE);
            loadingimage.setProgress(values[0]);
        }

    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    public boolean fileExistance(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    private void SaveImage(Bitmap bmp, String iName){
        try {
            FileOutputStream outputStream = openFileOutput(iName + ".png", Context.MODE_PRIVATE);
            bmp.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
            outputStream.flush();
            outputStream.close();
        }catch (FileNotFoundException ex){
            Log.i("FileNotFoundException",ex.getMessage());
        }catch(IOException ex){
            Log.i("IOException",ex.getMessage());
        }
    }

    public static Bitmap getImage(URL url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                return BitmapFactory.decodeStream(connection.getInputStream());
            } else
                return null;
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
