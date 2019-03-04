package com.theevilone.weatherapp.HelperClasses;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.theevilone.weatherapp.CurrentWeather.CurrentWeather;
import com.theevilone.weatherapp.FiveDayForecastWeather.FiveDayWeather;

public class CustomSharedPreferences {


    SharedPreferences sharedPreferences;
    Activity activity;

    public CustomSharedPreferences(Activity activity) {
        this.activity = activity;
        sharedPreferences = this.activity.getSharedPreferences(StaticStrings.SHARED_PREFERENCES, activity.getApplicationContext().MODE_PRIVATE);
    }

    public void putCurrentWeather(String key, CurrentWeather currentWeather) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key + "_temperature", currentWeather.getTemperature());
        editor.putString(key + "_icon", currentWeather.getIcon());
        editor.putString(key + "_weatherText", currentWeather.getWeatherText());
        editor.putString(key + "_minTemperature", currentWeather.getMinTemperature());
        editor.putString(key + "_maxTemperature", currentWeather.getMinTemperature());
        editor.apply();
    }

    public CurrentWeather getCurrentWeather(String key) {
        CurrentWeather currentWeather = new CurrentWeather();

        currentWeather.setTemperature(sharedPreferences.getString(key + "_temperature", ""));
        currentWeather.setIcon(sharedPreferences.getString(key + "_icon", ""));
        currentWeather.setWeatherText(sharedPreferences.getString(key + "_weatherText", ""));
        currentWeather.setMinTemperature(sharedPreferences.getString(key + "_minTemperature", ""));
        currentWeather.setMaxTemperature(sharedPreferences.getString(key + "_maxTemperature", ""));

        return currentWeather;
    }

    public void putFiveDayForecastWeather(String key, FiveDayWeather fiveDayWeather)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for(int i=0;i<fiveDayWeather.getDay().size();i++)
        {
            editor.putInt(key + "_size",fiveDayWeather.getDay().size());
            Log.i("CUSTON SHERED", String.valueOf(fiveDayWeather.getDay().size()));
            editor.putString(key+"_day"+i,fiveDayWeather.getDay().get(i));
            editor.putString(key+"_image"+i,fiveDayWeather.getImage().get(i));
            editor.putString(key+"_tempMax"+i,fiveDayWeather.getTemperatureMax().get(i));
            editor.putString(key+"_tempMin"+i,fiveDayWeather.getTemperatureMin().get(i));
            editor.apply();
        }
    }

    public FiveDayWeather getFiveDayForecastWeather(String key)
    {
        FiveDayWeather fiveDayWeather = new FiveDayWeather();
        int size = sharedPreferences.getInt(key+"_size", 0);
        Log.i("CUSTON SHERED", String.valueOf(size));
        for(int i=0;i<size;i++)
        {
            Log.i("CUSTON SHERED I: ", String.valueOf(i));
            fiveDayWeather.getDay().add(sharedPreferences.getString(key+"_day"+i, ""));
            fiveDayWeather.getImage().add(sharedPreferences.getString(key+"_image"+i, ""));
            fiveDayWeather.getTemperatureMax().add(sharedPreferences.getString(key+"_tempMax"+i, ""));
            fiveDayWeather.getTemperatureMin().add(sharedPreferences.getString(key+"_tempMin"+i, ""));
        }

        return  fiveDayWeather;
    }

}
