package com.example.weatherapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText city;
    DownloadText task;
    TextView showWeather;
    TextView showTemp;
    ImageView  imageView;
    TextView wind;

    public void showWeather(View view){

        task = new DownloadText();

        String chosenCity = city.getText().toString();

        task.execute("https://api.apixu.com/v1/current.json?key=bcce1c09bfe44016a86101641192805&q=" + chosenCity);

        city.setHint("City");

        city.setText("");

    }

    public class DownloadText extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {

            URL url;
            HttpURLConnection urlConnection = null;
            String result = "";

            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data;
                data = reader.read();

                while(data!=-1){
                    char ch = (char)data;
                    result+=ch;

                    data = reader.read();
                }

                Log.i("Result", result);

                return result;


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                JSONObject jsonObject = new JSONObject(result);
                JSONObject weather = new JSONObject(jsonObject.getString("current"));
                JSONObject details = new JSONObject(weather.getString("condition"));



                String st = details.getString("icon").substring(2);

                showWeather.setVisibility(View.VISIBLE);
                showTemp.setVisibility(View.VISIBLE);
                wind.setVisibility(View.VISIBLE);

                String s = weather.getString("temp_c") + " *C";
                String wSpeed = "Wind Speed: " + weather.getString("wind_kph") + " kph";

                Log.i("Wind", wSpeed);

                showTemp.setText(s);
                showWeather.setText(details.getString("text"));
                wind.setText(wSpeed);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = (EditText) findViewById(R.id.cityName);
        showWeather = (TextView) findViewById(R.id.Weather);
        showTemp = (TextView)findViewById(R.id.Temp);
        wind = (TextView)findViewById(R.id.Wind);
    }
}
