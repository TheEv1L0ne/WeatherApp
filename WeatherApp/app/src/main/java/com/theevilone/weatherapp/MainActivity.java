package com.theevilone.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.theevilone.weatherapp.CurrentWeather.CurrentWeather;
import com.theevilone.weatherapp.CurrentWeather.FragmentCurrentWeather;
import com.theevilone.weatherapp.FiveDayForecastWeather.FragmentFiveDayWeather;
import com.theevilone.weatherapp.HelperClasses.CustomSharedPreferences;
import com.theevilone.weatherapp.HelperClasses.StaticStrings;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;


    private Button refreshWeatherData;
    private Button settings;

    private FragmentCurrentWeather fragmentCurrentWeather;
    private FragmentFiveDayWeather fragmentFiveDayWeather;

    SharedPreferences sharedpreferences;
    CustomSharedPreferences customSharedPreferences;

    public static Activity staticMainActivity = null;

    TextView cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(staticMainActivity == null)
        {
            staticMainActivity = MainActivity.this;
        }

        sharedpreferences = getSharedPreferences(StaticStrings.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        customSharedPreferences = new CustomSharedPreferences(this);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager_id);

        //Creating Tab Fragments
        fragmentCurrentWeather = new FragmentCurrentWeather();
        fragmentFiveDayWeather = new FragmentFiveDayWeather();

        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());
        adapter.AddFragment(fragmentCurrentWeather, "Current");
        adapter.AddFragment(fragmentFiveDayWeather, "5 Days");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        cityName = findViewById(R.id.tv_selected_city);
        cityName.setText("PERIVOJE");

        refreshWeatherData = findViewById(R.id.btn_refresh_data);
        refreshWeatherData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChooseCityDialog chooseCityDialog = new ChooseCityDialog();
                chooseCityDialog.SetActivity(MainActivity.this);
                chooseCityDialog.show(getSupportFragmentManager(), "choose_dialog");


            }
        });

        settings = findViewById(R.id.btn_settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsDialog settingsDialog = new SettingsDialog();
                settingsDialog.SetActivity(MainActivity.this);
                settingsDialog.show(getSupportFragmentManager(), "settings_dialog");
            }
        });



    }

    public void setCityName(String name)
    {
        cityName.setText(name);
    }

    public void parseData()
    {
        fragmentCurrentWeather.parseDataForCurrent();
        fragmentFiveDayWeather.parseDataForFiveDaysForcast();
    }

}
