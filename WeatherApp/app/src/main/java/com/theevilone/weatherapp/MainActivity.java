package com.theevilone.weatherapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.theevilone.weatherapp.CurrentWeather.FragmentCurrentWeather;
import com.theevilone.weatherapp.FiveDayForecastWeather.FragmentFiveDayWeather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;


    private Button refreshWeatherData;
    private Button settings;

    private FragmentCurrentWeather fragmentCurrentWeather;
    private FragmentFiveDayWeather fragmentFiveDayWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager_id);


        fragmentCurrentWeather = new FragmentCurrentWeather();
        fragmentFiveDayWeather = new FragmentFiveDayWeather();

        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());
        adapter.AddFragment(fragmentCurrentWeather, "Current");
        adapter.AddFragment(fragmentFiveDayWeather, "5 Days");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        refreshWeatherData = findViewById(R.id.btn_refresh_data);
        refreshWeatherData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentCurrentWeather.parseDataForCurrent(MainActivity.this);
                fragmentFiveDayWeather.parseDataForFiveDaysForcast(MainActivity.this);


            }
        });

        settings = findViewById(R.id.btn_settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsDialog settingsDialog = new SettingsDialog();
                settingsDialog.show(getSupportFragmentManager(), "settings_dialog");
            }
        });
    }
}
