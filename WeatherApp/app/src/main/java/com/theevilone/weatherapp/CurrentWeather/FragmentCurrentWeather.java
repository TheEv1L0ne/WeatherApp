package com.theevilone.weatherapp.CurrentWeather;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.theevilone.weatherapp.HelperClasses.CustomSharedPreferences;
import com.theevilone.weatherapp.HelperClasses.StaticStrings;
import com.theevilone.weatherapp.MainActivity;
import com.theevilone.weatherapp.R;

public class FragmentCurrentWeather extends Fragment {

    View view;

    TextView currentWeather;
    TextView currentWeatherDescription;
    TextView humidity;
    TextView pressure;
    ImageView currentImage;

    JsonTaskForCurrentWeather jsonTaskForCurrentWeather;
    MainActivity mainActivity;

    public FragmentCurrentWeather() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab1_current_weather, container, false);

        currentWeather = view.findViewById(R.id.tv_current_temperature);
        currentWeatherDescription = view.findViewById(R.id.tv_current_weather_description);
        humidity = view.findViewById(R.id.humidity);
        pressure = view.findViewById(R.id.pressure);
        currentImage = view.findViewById(R.id.img_weather_image);

//        refreshCurrentWeatherData();

        SharedPreferences sharedpreferences = MainActivity.staticMainActivity.getSharedPreferences(StaticStrings.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        if(sharedpreferences.getInt(StaticStrings.GET_DATA_FOR_FIRST_TIME_CURRENT,-1) == -1)
        {
            Log.i("ToastLog", "First time here");
            parseDataForCurrent();
            sharedpreferences.edit().putInt(StaticStrings.GET_DATA_FOR_FIRST_TIME_CURRENT, 0).apply();
        }
        else
        {
            Log.i("ToastLog", "Not First time here");
            CustomSharedPreferences customSharedPreferences = new CustomSharedPreferences(MainActivity.staticMainActivity);
            CurrentWeather currentWeather = customSharedPreferences.getCurrentWeather(StaticStrings.CURRENT_WEATHER_DATA_KEY);
            refreshCurrentWeatherData(currentWeather);
        }

        return view;
    }

    public void setMainActivity(MainActivity mainActivity)
    {
        this.mainActivity = mainActivity;
    }

    public void parseDataForCurrent()
    {
        jsonTaskForCurrentWeather = new JsonTaskForCurrentWeather(MainActivity.staticMainActivity, this);
        jsonTaskForCurrentWeather.startTask();
    }

    public void refreshCurrentWeatherData(CurrentWeather cw)
    {

        //Storing data
        CustomSharedPreferences customSharedPreferences = new CustomSharedPreferences(MainActivity.staticMainActivity);
        customSharedPreferences.putCurrentWeather(StaticStrings.CURRENT_WEATHER_DATA_KEY, cw);

        currentWeather.setText(cw.getTemperature()+ "°C");
        currentWeatherDescription.setText(cw.getWeatherText());
        humidity.setText("Humidity: " + cw.getHumidity() + "%");
        pressure.setText("Pressure: " + cw.getPressure() + "mb");
        currentImage.setImageDrawable(ContextCompat.getDrawable(getActivity(),getImage(cw.getIcon())));
    }

    // (C *9/5)+32 = F
    public void convertCtoF()
    {

        //FINISH THIS

        CustomSharedPreferences customSharedPreferences = new CustomSharedPreferences(MainActivity.staticMainActivity);
        CurrentWeather currentWeather = customSharedPreferences.getCurrentWeather(StaticStrings.CURRENT_WEATHER_DATA_KEY);

        currentWeather.setTemperature(currentWeather.getTemperature());
    }

    private int getImage(String imageCode)
    {
        int imageDecoded = 0;

        switch(imageCode)
        {
            case "01d":
                imageDecoded = R.drawable.image40;
                break;
            case "02d":
                imageDecoded = R.drawable.image2;
                break;
            case "03d":
                imageDecoded = R.drawable.image1;
                break;
            case "04d":
                imageDecoded = R.drawable.image1;
                break;
            case "09d":
                imageDecoded = R.drawable.image11;
                break;
            case "10d":
                imageDecoded = R.drawable.image5;
                break;
            case "11d":
                imageDecoded = R.drawable.image38;
                break;
            case "13d":
                imageDecoded = R.drawable.image26;
                break;
            case "50d":
                imageDecoded = R.drawable.image29;
                break;
            case "50n":
                imageDecoded = R.drawable.image30;
                break;
            case "01n":
                imageDecoded = R.drawable.image45;
                break;
            case "02n":
                imageDecoded = R.drawable.image3;
                break;
            case "03n":
                imageDecoded = R.drawable.image1;
                break;
            case "04n":
                imageDecoded = R.drawable.image1;
                break;
            case "09n":
                imageDecoded = R.drawable.image12;
                break;
            case "10n":
                imageDecoded = R.drawable.image6;
                break;
            case "11n":
                imageDecoded = R.drawable.image39;
                break;
            case "13n":
                imageDecoded = R.drawable.image27;
                break;
                default:
                    imageDecoded = R.drawable.image40;
        }
        return imageDecoded;
    }

}