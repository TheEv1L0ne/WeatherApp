package com.theevilone.weatherapp.FiveDayForecastWeather;

import android.util.Log;

import com.theevilone.weatherapp.CurrentWeather.CurrentWeather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class JSONParserForFiveDayForecastWeather {

    protected String jsonString;

    public JSONParserForFiveDayForecastWeather(String jsonString) {
        this.jsonString = jsonString;
    }

    public FiveDayWeather Parse() throws JSONException {

        FiveDayWeather fiveDayWeather = new FiveDayWeather();

        JSONObject jObject = new JSONObject(jsonString);
        JSONArray list = jObject.getJSONArray("list");

        int j = -1;

        List<String> days = new ArrayList<>();
        List<String> images = new ArrayList<>();
        List<String> temperatureMin = new ArrayList<>();
        List<String> temperatureMax = new ArrayList<>();

        int startMin = 0;
        int startMax = 0;

        //Going through list of "3 hours forecast data" for "5 day forecast" api call
        for (int i=0; i < list.length(); i++)
        {
            try {
                JSONObject oneObject = list.getJSONObject(i);
                // Pulling items from the array

                String time = oneObject.getString("dt_txt");

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = format.parse(time);
                String dayOfTheWeek = new SimpleDateFormat("EEEE").format(date);
                String dayOfTheWeek1 = new SimpleDateFormat("dd").format(date);

                int day = Integer.parseInt(dayOfTheWeek1);

                JSONArray weather = oneObject.getJSONArray("weather");

                String icon = "";

                //Going through weather description for each hour listed
                for (int k=0; k < weather.length(); k++)
                {
                    try {
                        JSONObject oneObject1 = weather.getJSONObject(k);
                        // Pulling items from the array
                        icon = oneObject1.getString("icon");
                        icon = icon.substring(0, icon.length()-1) + "d"; // Changing last character so Image is always for day and for night
                    } catch (JSONException e) {
                        // Oops
                    }
                }

                //Getting Min and Max temperature
                String main = oneObject.getString("main");
                JSONObject mainObject = new JSONObject(main);

                String tMin = mainObject.getString("temp_min");
                String tMax = mainObject.getString("temp_max");


                int tempMin = (Float.valueOf(tMin).intValue());
                int tempMax = (Float.valueOf(tMax)).intValue();


                if(j==-1 || j != day)
                {
                    days.add(dayOfTheWeek);
                    images.add(icon);
                    j = day;

                    startMax = tempMax;
                    startMin = tempMin;


                    temperatureMax.add(String.valueOf(tempMax));
                    temperatureMin.add(String.valueOf(tempMin));
                }
                else if(j == day)
                {

                    if(tempMax > startMax) {
                        startMax = tempMax;
                        temperatureMax.set(temperatureMax.size()-1,String.valueOf(tempMax));
                    }

                    if(tempMin < startMin) {
                        startMin = tempMin;
                        temperatureMin.set(temperatureMin.size()-1,String.valueOf(tempMin));
                    }


                }



            } catch (JSONException e) {
                // Oops
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //Finally adding data to data model
        fiveDayWeather.setDay(days);
        fiveDayWeather.setImage(images);
        fiveDayWeather.setTemperatureMax(temperatureMax);
        fiveDayWeather.setTemperatureMin(temperatureMin);

        return fiveDayWeather;

    }

}
