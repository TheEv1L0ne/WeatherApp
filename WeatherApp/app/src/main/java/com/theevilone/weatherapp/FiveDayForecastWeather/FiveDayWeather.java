package com.theevilone.weatherapp.FiveDayForecastWeather;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class FiveDayWeather {

    List<String> day = new ArrayList<>();
    List<String> image  = new ArrayList<>();;
    List<String> temperatureMin  = new ArrayList<>();
    List<String> temperatureMax  = new ArrayList<>();

    public FiveDayWeather() {
    }


    public List<String> getDay() {
        return day;
    }

    public void setDay(List<String> day) {
        this.day = day;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public List<String> getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(List<String> temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public List<String> getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(List<String> temperatureMax) {
        this.temperatureMax = temperatureMax;
    }



}
