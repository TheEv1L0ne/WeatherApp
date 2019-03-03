package com.theevilone.weatherapp.JSONParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;


import com.theevilone.weatherapp.DataModels.CurrentWeather;
import com.theevilone.weatherapp.FragmentCurrentWeather;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JsonTaskForCurrentWeather {

    private Activity activityInstance;
    private FragmentCurrentWeather fragmentCurrentWeather;

    public JsonTaskForCurrentWeather( Activity activityInstance, FragmentCurrentWeather fragmentCurrentWeather) {
        this.activityInstance = activityInstance;
        this.fragmentCurrentWeather = fragmentCurrentWeather;
    }

    ProgressDialog pd;

    public void startTask()
    {
        new JsonTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,  "http://api.openweathermap.org/data/2.5/weather?q=Belgrade&units=metric&APPID=3e1c8affc8fa507636e25753c5d43afb");
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

            JSONParserForCurrentWeather jsonParser = new JSONParserForCurrentWeather(result);
            try {
                CurrentWeather currentWeather = jsonParser.Parse();
                fragmentCurrentWeather.refreshCurrentWeatherData(currentWeather);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
