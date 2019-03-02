package com.theevilone.weatherapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentCurrentWeather extends Fragment {

    View view;

    TextView current_weather;

    public FragmentCurrentWeather() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab1_current_weather, container, false);

        current_weather = view.findViewById(R.id.tv_current_temperature);

        return view;
    }

    //TESTING FUNCTION!!!
    public void SetFragmentName()
    {
        current_weather.setText("STAVIO SAM TEXT HEHE");
    }
}
