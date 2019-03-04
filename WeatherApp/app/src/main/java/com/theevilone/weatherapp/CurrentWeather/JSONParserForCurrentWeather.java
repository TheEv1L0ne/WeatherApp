package com.theevilone.weatherapp.CurrentWeather;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParserForCurrentWeather {

    protected String jsonString;

    public JSONParserForCurrentWeather(String jsonString) {
        this.jsonString = jsonString;
    }

    public CurrentWeather Parse() throws JSONException {
        JSONObject jObject = new JSONObject(jsonString);
        String aJsonString = jObject.getString("main");
        Log.i("JSONLOG", aJsonString);

        JSONObject jObjectMain = new JSONObject(aJsonString);
        String temp = jObjectMain.getString("temp");
        String temp_max = jObjectMain.getString("temp_max");
        String temp_min = jObjectMain.getString("temp_min");

        temp = String.valueOf(Float.valueOf(temp).intValue());
        temp_max = String.valueOf(Float.valueOf(temp_max).intValue());
        temp_min = String.valueOf(Float.valueOf(temp_min).intValue());

        String icon="";
        String description="";

        JSONArray jArray = jObject.getJSONArray("weather");
        for (int i=0; i < jArray.length(); i++)
        {
            try {
                JSONObject oneObject = jArray.getJSONObject(i);
                // Pulling items from the array
                icon = oneObject.getString("icon");
                description = oneObject.getString("description");
            } catch (JSONException e) {
                // Oops
            }
        }

        Log.i("JSONLOG", temp);
        Log.i("JSONLOG", temp_max);
        Log.i("JSONLOG", temp_min);
        Log.i("JSONLOG", icon);
        Log.i("JSONLOG", description);

        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setTemperature(temp);
        currentWeather.setIcon(icon);
        currentWeather.setMaxTemperature(temp_max);
        currentWeather.setMinTemperature(temp_min);
        currentWeather.setWeatherText(description);

        return currentWeather;

    }



}
