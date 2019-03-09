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
        String cityName = jObject.getString("name");
        String aJsonString = jObject.getString("main");

        JSONObject jObjectMain = new JSONObject(aJsonString);
        String temp = jObjectMain.getString("temp");
        String humidity = jObjectMain.getString("humidity");
        String pressure = jObjectMain.getString("pressure");

        temp = String.valueOf(Float.valueOf(temp).intValue());
        humidity = String.valueOf(Float.valueOf(humidity).intValue());
        pressure = String.valueOf(Float.valueOf(pressure).intValue());

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


        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setCityName(cityName);
        currentWeather.setTemperature(temp);
        currentWeather.setIcon(icon);
        currentWeather.setHumidity(humidity);
        currentWeather.setPressure(pressure);
        currentWeather.setWeatherText(description);

        return currentWeather;

    }



}
