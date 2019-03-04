package com.theevilone.weatherapp.CurrentWeather;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.theevilone.weatherapp.R;

public class FragmentCurrentWeather extends Fragment {

    View view;

    TextView currentWeather;
    TextView currentWeatherDescription;
    TextView currentMinAndMax;
    ImageView currentImage;

    JsonTaskForCurrentWeather jsonTaskForCurrentWeather;

    public FragmentCurrentWeather() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab1_current_weather, container, false);

        currentWeather = view.findViewById(R.id.tv_current_temperature);
        currentWeatherDescription = view.findViewById(R.id.tv_current_weather_description);
        currentMinAndMax = view.findViewById(R.id.tv_min_and_max_current);
        currentImage = view.findViewById(R.id.img_weather_image);

//        refreshCurrentWeatherData();

        return view;
    }

    public void parseDataForCurrent(Activity activity)
    {
        jsonTaskForCurrentWeather = new JsonTaskForCurrentWeather(activity, this);
        jsonTaskForCurrentWeather.startTask();
    }

    public void refreshCurrentWeatherData(CurrentWeather cw)
    {
        currentWeather.setText(cw.getTemperature());
        currentWeatherDescription.setText(cw.getWeatherText());
        currentMinAndMax.setText(cw.getMinTemperature() + "/" + cw.getMaxTemperature());
        currentImage.setImageDrawable(ContextCompat.getDrawable(getActivity(),getImage(cw.getIcon())));
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
