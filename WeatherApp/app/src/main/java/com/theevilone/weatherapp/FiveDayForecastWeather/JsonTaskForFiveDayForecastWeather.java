package com.theevilone.weatherapp.FiveDayForecastWeather;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.theevilone.weatherapp.CurrentWeather.CurrentWeather;
import com.theevilone.weatherapp.CurrentWeather.FragmentCurrentWeather;
import com.theevilone.weatherapp.CurrentWeather.JSONParserForCurrentWeather;
import com.theevilone.weatherapp.CurrentWeather.JsonTaskForCurrentWeather;
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

    public JsonTaskForFiveDayForecastWeather( Activity activityInstance, FragmentFiveDayWeather fragmentFiveDayWeather) {
        this.activityInstance = activityInstance;
        this.fragmentFiveDayWeather = fragmentFiveDayWeather;
    }

    ProgressDialog pd;

    public void startTask()
    {

        sharedpreferences = MainActivity.staticMainActivity.getSharedPreferences(StaticStrings.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String apiUrl = "";
        int selected = sharedpreferences.getInt(StaticStrings.UNITS_SELECTED, 0);
        if(selected == 0)
            apiUrl = "http://api.openweathermap.org/data/2.5/" + StaticStrings.API_FORECAST + "Belgrade" + "&units=" + StaticStrings.METRIC_UNITS + "&APPID=" +StaticStrings.API_KEY;
        else
            apiUrl = "http://api.openweathermap.org/data/2.5/" + StaticStrings.API_FORECAST + "Belgrade" + "&units=" + StaticStrings.IMPERIAL_UNITS + "&APPID=" +StaticStrings.API_KEY;

        new JsonTaskForFiveDayForecastWeather.JsonTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,  apiUrl);
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(activityInstance);
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

            JSONParserForFiveDayForecastWeather jsonParser = new JSONParserForFiveDayForecastWeather(result);
            try {
                FiveDayWeather fiveDayWeather = jsonParser.Parse();
                fragmentFiveDayWeather.refreshCurrentWeatherData(fiveDayWeather);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}