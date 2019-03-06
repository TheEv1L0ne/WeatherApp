package com.theevilone.weatherapp.FiveDayForecastWeather;

import android.app.Activity;
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

import com.theevilone.weatherapp.CurrentWeather.CurrentWeather;
import com.theevilone.weatherapp.CurrentWeather.JsonTaskForCurrentWeather;
import com.theevilone.weatherapp.HelperClasses.CustomSharedPreferences;
import com.theevilone.weatherapp.HelperClasses.StaticStrings;
import com.theevilone.weatherapp.MainActivity;
import com.theevilone.weatherapp.R;

public class FragmentFiveDayWeather extends Fragment {

    View view;

    TextView weatherDay[] = new TextView[5];
    ImageView weatherImage[] = new ImageView[5];
    TextView weatherTemperature[] = new TextView[5];

    JsonTaskForFiveDayForecastWeather jsonTaskForFiveDayForecastWeather;

    public FragmentFiveDayWeather() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab2_five_day_weather, container, false);

        weatherDay[0] = view.findViewById(R.id.tv_day1);
        weatherImage[0] = view.findViewById(R.id.img_icon1);
        weatherTemperature[0] = view.findViewById(R.id.tv_temperature1);

        weatherDay[1] = view.findViewById(R.id.tv_day2);
        weatherImage[1] = view.findViewById(R.id.img_icon2);
        weatherTemperature[1] = view.findViewById(R.id.tv_temperature2);

        weatherDay[2] = view.findViewById(R.id.tv_day3);
        weatherImage[2] = view.findViewById(R.id.img_icon3);
        weatherTemperature[2] = view.findViewById(R.id.tv_temperature3);

        weatherDay[3] = view.findViewById(R.id.tv_day4);
        weatherImage[3] = view.findViewById(R.id.img_icon4);
        weatherTemperature[3] = view.findViewById(R.id.tv_temperature4);

        weatherDay[4] = view.findViewById(R.id.tv_day5);
        weatherImage[4] = view.findViewById(R.id.img_icon5);
        weatherTemperature[4] = view.findViewById(R.id.tv_temperature5);

        SharedPreferences sharedpreferences = MainActivity.staticMainActivity.getSharedPreferences(StaticStrings.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        if(sharedpreferences.getInt(StaticStrings.GET_DATA_FOR_FIRST_TIME_FIVE_DAY,-1) == 0)
        {
            Log.i("ToastLog", "Not First time here");
            CustomSharedPreferences customSharedPreferences = new CustomSharedPreferences(MainActivity.staticMainActivity);
            FiveDayWeather fiveDayWeather = customSharedPreferences.getFiveDayForecastWeather(StaticStrings.FIVE_DAY_WEATHER_DATA_KEY);
            refreshCurrentWeatherData(fiveDayWeather);
        }

        return view;
    }

    public void parseDataForFiveDaysForcast()
    {
        jsonTaskForFiveDayForecastWeather = new JsonTaskForFiveDayForecastWeather(MainActivity.staticMainActivity, this);
        jsonTaskForFiveDayForecastWeather.startTask();
    }

    public void refreshCurrentWeatherData(FiveDayWeather fdw)
    {

        //Storing data
        CustomSharedPreferences customSharedPreferences = new CustomSharedPreferences(MainActivity.staticMainActivity);
        customSharedPreferences.putFiveDayForecastWeather(StaticStrings.FIVE_DAY_WEATHER_DATA_KEY, fdw);

        //Setting data into view
        if(fdw.getDay().size() != 0) { // checking if there is data to show
            for (int i = 0; i < weatherDay.length; i++) {
                SharedPreferences sharedpreferences = MainActivity.staticMainActivity.getSharedPreferences(StaticStrings.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                int selected = sharedpreferences.getInt(StaticStrings.UNITS_SELECTED, 0);

                weatherDay[i].setText(fdw.getDay().get(i));
                if (selected == 0)
                    weatherTemperature[i].setText(fdw.getTemperatureMin().get(i) + "°C/" + fdw.getTemperatureMax().get(i) + "°C");
                else
                    weatherTemperature[i].setText(fdw.getTemperatureMin().get(i) + "°F/" + fdw.getTemperatureMax().get(i) + "°F");
                weatherImage[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), getImage(fdw.getImage().get(i))));
            }
        }
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
