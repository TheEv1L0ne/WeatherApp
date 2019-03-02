package com.theevilone.weatherapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;


    Button refreshWeatherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager_id);



        final FragmentCurrentWeather fragmentCurrentWeather = new FragmentCurrentWeather();

        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());
        adapter.AddFragment(fragmentCurrentWeather, "Current");
        adapter.AddFragment(new FragmentFiveDayWeather(), "Five Days Forecast");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        refreshWeatherData = findViewById(R.id.btn_refresh_data);
        refreshWeatherData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(!cityName.getText().toString().isEmpty()) {
//                    //forecast
////                    new JsonTask().execute("http://api.openweathermap.org/data/2.5/weather?q=" + cityName.getText().toString() +"&units=metric&APPID=3e1c8affc8fa507636e25753c5d43afb");
//                    new JsonTask().execute("http://api.openweathermap.org/data/2.5/forecast?q=" + cityName.getText().toString() +"&units=metric&APPID=3e1c8affc8fa507636e25753c5d43afb");
//                }
            }
        });
    }

    ProgressDialog pd;

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;
            InputStream is = null;

            try {
                URL url = new URL(params[0]);
                Log.i("RESSULTT", params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.connect();

                // Let's read the response
                StringBuffer buffer = new StringBuffer();
                is = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = null;
                while (  (line = br.readLine()) != null )
                    buffer.append(line + "\r\n");

                is.close();
                connection.disconnect();
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result!=null)
            if (pd.isShowing()){
                pd.dismiss();
            }
            if(result!=null)
                Log.i("RESSULTT", result);
            else
            {
                Log.i("RESSULTT", "JEBENI NULL");
            }
        }
    }
}
