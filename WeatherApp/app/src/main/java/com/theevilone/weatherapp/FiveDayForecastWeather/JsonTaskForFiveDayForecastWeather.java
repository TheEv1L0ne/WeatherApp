package com.theevilone.weatherapp.FiveDayForecastWeather;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.theevilone.weatherapp.CurrentWeather.CurrentWeather;
import com.theevilone.weatherapp.CurrentWeather.FragmentCurrentWeather;
import com.theevilone.weatherapp.CurrentWeather.JSONParserForCurrentWeather;
import com.theevilone.weatherapp.CurrentWeather.JsonTaskForCurrentWeather;
import com.theevilone.weatherapp.HelperClasses.CustomSharedPreferences;
import com.theevilone.weatherapp.HelperClasses.StaticStrings;
import com.theevilone.weatherapp.MainActivity;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JsonTaskForFiveDayForecastWeather {

    private Activity activityInstance;
    private FragmentFiveDayWeather fragmentFiveDayWeather;

    SharedPreferences sharedpreferences;

    public JsonTaskForFiveDayForecastWeather(Activity activityInstance, FragmentFiveDayWeather fragmentFiveDayWeather) {
        this.activityInstance = activityInstance;
        this.fragmentFiveDayWeather = fragmentFiveDayWeather;
    }

    ProgressDialog pd;

    private boolean error = false;

    public void startTask() {

        error = false;

        sharedpreferences = MainActivity.staticMainActivity.getSharedPreferences(StaticStrings.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        int selected = sharedpreferences.getInt(StaticStrings.UNITS_SELECTED, 0);

        String apiUrl = "";
        if (sharedpreferences.getInt(StaticStrings.GET_DATA_FOR_FIRST_TIME_FIVE_DAY, -1) == -1) {
            sharedpreferences.edit().putInt(StaticStrings.GET_DATA_FOR_FIRST_TIME_FIVE_DAY, 0).apply();
            //api.openweathermap.org/data/2.5/weather?lat=35&lon=139
            if (selected == 0)
                apiUrl = "http://api.openweathermap.org/data/2.5/forecast?lat=" + sharedpreferences.getString("LAT", "") + "&lon=" + sharedpreferences.getString("LON", "") + "&units=" + StaticStrings.METRIC_UNITS + "&APPID=" + StaticStrings.API_KEY;
            else
                apiUrl = "http://api.openweathermap.org/data/2.5/forecast?lat=" + sharedpreferences.getString("LAT", "") + "&lon=" + sharedpreferences.getString("LON", "") + "&units=" + StaticStrings.IMPERIAL_UNITS + "&APPID=" + StaticStrings.API_KEY;

        } else {

            String cityName = sharedpreferences.getString(StaticStrings.CITY_TO_SEARCH, "");

            if (selected == 0)
                apiUrl = "http://api.openweathermap.org/data/2.5/" + StaticStrings.API_FORECAST + cityName + "&units=" + StaticStrings.METRIC_UNITS + "&APPID=" + StaticStrings.API_KEY;
            else
                apiUrl = "http://api.openweathermap.org/data/2.5/" + StaticStrings.API_FORECAST + cityName + "&units=" + StaticStrings.IMPERIAL_UNITS + "&APPID=" + StaticStrings.API_KEY;
        }

        new JsonTaskForFiveDayForecastWeather.JsonTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, apiUrl);
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

//            pd = new ProgressDialog(activityInstance);
//            pd.setMessage("Please wait");
//            pd.setCancelable(false);
//            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;
            InputStream is = null;

            try {
                URL url = new URL(params[0]);
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
                while ((line = br.readLine()) != null)
                    buffer.append(line + "\r\n");

                is.close();
                connection.disconnect();
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                error = true;
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


            if (result != null & error == false) {

                JSONParserForFiveDayForecastWeather jsonParser = new JSONParserForFiveDayForecastWeather(result);
                try {
                    FiveDayWeather fiveDayWeather = jsonParser.Parse();
                    fragmentFiveDayWeather.refreshCurrentWeatherData(fiveDayWeather);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

                CustomSharedPreferences customSharedPreferences = new CustomSharedPreferences(MainActivity.staticMainActivity);
                CurrentWeather currentWeather = customSharedPreferences.getCurrentWeather(StaticStrings.CURRENT_WEATHER_DATA_KEY);
                sharedpreferences.edit().putString(StaticStrings.CITY_TO_SEARCH, currentWeather.getCityName()).apply();

                Toast.makeText(MainActivity.staticMainActivity, "No city found!", Toast.LENGTH_LONG).show();
            }


        }
    }
}
