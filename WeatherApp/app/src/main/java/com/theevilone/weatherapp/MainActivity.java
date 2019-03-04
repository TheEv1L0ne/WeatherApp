package com.theevilone.weatherapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
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

    //Settings dialog
    LinearLayout settingsDialog;
    Button btnSettingsCancel;
    Button btnSettingsOk;
    private Switch celsiusCheckBox;
    private Switch fahrenheitCheckBox;

    //Search Dialog
    LinearLayout searchDialog;
    Button btnSearchOk;
    Button btnSearchCancel;
    EditText searchText;


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

        //SETTINGS - START

        settingsDialog = findViewById(R.id.dialog_settings);

        settings = findViewById(R.id.btn_settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsDialog.setVisibility(View.VISIBLE);

            }
        });

        btnSettingsCancel = findViewById(R.id.btn_cancel);
        btnSettingsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsDialog.setVisibility(View.GONE);
            }
        });

        btnSettingsOk = findViewById(R.id.btn_ok);
        btnSettingsOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseData();
                settingsDialog.setVisibility(View.GONE);
            }
        });

        celsiusCheckBox = findViewById(R.id.switch1);
        fahrenheitCheckBox = findViewById(R.id.switch2);

        int selected = sharedpreferences.getInt(StaticStrings.UNITS_SELECTED, 0);
        if(selected == 0) {
            Log.i("Settings: ", "ZERO");
            celsiusCheckBox.setChecked(true);
            fahrenheitCheckBox.setChecked(false);
        }
        else
        {
            Log.i("Settings: ", "UNO");
            celsiusCheckBox.setChecked(false);
            fahrenheitCheckBox.setChecked(true);
        }

        celsiusCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    fahrenheitCheckBox.setChecked(false);
                    sharedpreferences.edit().putInt(StaticStrings.UNITS_SELECTED, 0).apply();
                }
                else
                {
                    fahrenheitCheckBox.setChecked(true);
                    sharedpreferences.edit().putInt(StaticStrings.UNITS_SELECTED, 1).apply();
                }
            }
        });

        fahrenheitCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    celsiusCheckBox.setChecked(false);
                    sharedpreferences.edit().putInt(StaticStrings.UNITS_SELECTED, 1).apply();
                }
                else
                {
                    celsiusCheckBox.setChecked(true);
                    sharedpreferences.edit().putInt(StaticStrings.UNITS_SELECTED, 0).apply();
                }
            }
        });

        //SETTINGS - END
        //SEARCH - START

        searchDialog = findViewById(R.id.dialog_choose_city);

        refreshWeatherData = findViewById(R.id.btn_refresh_data);
        refreshWeatherData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchDialog.setVisibility(View.VISIBLE);

            }
        });

        btnSearchCancel = findViewById(R.id.btn_cancel_search);
        btnSearchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDialog.setVisibility(View.GONE);
            }
        });

        btnSearchOk = findViewById(R.id.btn_search);
        btnSearchOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(searchText.getText().toString().isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Please enter city name!",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    searchDialog.setVisibility(View.GONE);
                    parseData();
                }
            }
        });

        searchText = findViewById(R.id.et_search_city_name);

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
