package com.theevilone.weatherapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.theevilone.weatherapp.CurrentWeather.FragmentCurrentWeather;
import com.theevilone.weatherapp.FiveDayForecastWeather.FragmentFiveDayWeather;
import com.theevilone.weatherapp.HelperClasses.CustomSharedPreferences;
import com.theevilone.weatherapp.HelperClasses.GPSTracker;
import com.theevilone.weatherapp.HelperClasses.MultiSwipeRefreshLayout;
import com.theevilone.weatherapp.HelperClasses.StaticStrings;

import static com.theevilone.weatherapp.HelperClasses.HelperClass.isNetworkAvailable;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    ViewPager.OnPageChangeListener mOnPageChangeListener;

    Button refreshWeatherData;
    Button settings;

    private FragmentCurrentWeather fragmentCurrentWeather;
    private FragmentFiveDayWeather fragmentFiveDayWeather;

    SharedPreferences sharedpreferences;
    CustomSharedPreferences customSharedPreferences;

    public static Activity staticMainActivity = null;

    TextView cityName;

    //Settings dialog
    LinearLayout settingsDialog;
    LinearLayout settingsDialogMain;
    Button btnSettingsCancel;
    Button btnSettingsOk;
    private Switch celsiusCheckBox;
    private Switch fahrenheitCheckBox;

    //Search Dialog
    LinearLayout searchDialog;
    Button btnSearchOk;
    Button btnSearchCancel;
    AutoCompleteTextView searchText;
    LinearLayout searchMainDialog;

    //Frist time in app dialog
    LinearLayout firstTimeDialog;
    Button btnFirstOk;
    Button btnFirstCancel;
    LinearLayout firstDialog;

    ///
    boolean settingsOpen = false;
    boolean searchOpen = false;
    boolean firstOpen = false;

    //Animation
    Animation animation;

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
        fragmentCurrentWeather.setMainActivity(this);
        fragmentFiveDayWeather = new FragmentFiveDayWeather();

        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());
        adapter.AddFragment(fragmentCurrentWeather, "Current");
        adapter.AddFragment(fragmentFiveDayWeather, "5 Days");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        cityName = findViewById(R.id.tv_selected_city);

        //SETTINGS - START

        settingsDialog = findViewById(R.id.dialog_settings);
        settingsDialogMain = findViewById(R.id.dialog_settings_main);

        settings = findViewById(R.id.btn_settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                settingsDialog.setVisibility(View.VISIBLE);
                openDialogWithAnimation(settingsDialog,settingsDialogMain);
                settingsOpen = true;

            }
        });

        btnSettingsCancel = findViewById(R.id.btn_cancel);
        btnSettingsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                settingsDialog.setVisibility(View.GONE);
                closeDialogWithAnimation(settingsDialog,settingsDialogMain);
                settingsOpen = false;
            }
        });

        btnSettingsOk = findViewById(R.id.btn_ok);
        btnSettingsOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cityName = sharedpreferences.getString(StaticStrings.CITY_TO_SEARCH, "");
                if(!cityName.equalsIgnoreCase("") && isNetworkAvailable())
                    parseData();
//                settingsDialog.setVisibility(View.GONE);
                closeDialogWithAnimation(settingsDialog,settingsDialogMain);
                settingsOpen = false;
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
        searchMainDialog = findViewById(R.id.dialog_choose_main);

        refreshWeatherData = findViewById(R.id.btn_refresh_data);
        refreshWeatherData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                searchDialog.setVisibility(View.VISIBLE);
                openDialogWithAnimation(searchDialog,searchMainDialog);
                searchText.setText(""); //resets search field
                searchOpen = true;

            }
        });

        btnSearchCancel = findViewById(R.id.btn_cancel_search);
        btnSearchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Closes virtual keyboard
                searchText.clearFocus();
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                //Virtual keyboard must be called before closing view
//                searchDialog.setVisibility(View.GONE);
                closeDialogWithAnimation(searchDialog,searchMainDialog);
                searchOpen = false;

            }
        });

        btnSearchOk = findViewById(R.id.btn_search);
        btnSearchOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Closes virtual keyboard
                searchText.clearFocus();
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                //Virtual keyboard must be called before closing view
                if(searchText.getText().toString().isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Please enter city name!",
                            Toast.LENGTH_LONG).show();
                }
                else {
//                    searchDialog.setVisibility(View.GONE);
                    closeDialogWithAnimation(searchDialog,searchMainDialog);
                    sharedpreferences.edit().putString(StaticStrings.CITY_TO_SEARCH, searchText.getText().toString()).apply();
                    parseData();
                    searchOpen = false;
                }


            }
        });

//        searchText = findViewById(R.id.et_search_city_name);

        firstTimeDialog = findViewById(R.id.dialog_first_time_in_app);
        btnFirstOk = findViewById(R.id.btn_yes);
        btnFirstCancel = findViewById(R.id.btn_no);

        firstDialog = findViewById(R.id.first_dialog);


        if(sharedpreferences.getInt(StaticStrings.GET_DATA_FOR_FIRST_TIME_CURRENT,-1) == -1)
        {
            Log.i("ToastLog", "First time here");
//            firstTimeDialog.setVisibility(View.VISIBLE);
            openDialogWithAnimation(firstTimeDialog, firstDialog);
            firstOpen = true;

        }

        btnFirstCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                firstTimeDialog.setVisibility(View.GONE);
                closeDialogWithAnimation(firstTimeDialog, firstDialog);
                sharedpreferences.edit().putInt(StaticStrings.GET_DATA_FOR_FIRST_TIME_CURRENT,0).apply();
                sharedpreferences.edit().putInt(StaticStrings.GET_DATA_FOR_FIRST_TIME_FIVE_DAY,0).apply();
                firstOpen = false;
            }
        });

        btnFirstOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLocationPermission();
                closeDialogWithAnimation(firstTimeDialog, firstDialog);
//                firstTimeDialog.setVisibility(View.GONE);
                firstOpen = false;
            }
        });


        //Refreshes data when wiped down
        mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float v, int i1) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            //Disables refresh down while swiping left or right
            @Override
            public void onPageScrollStateChanged(int state) {
                if (pullToRefresh != null) {
                    pullToRefresh.setEnabled(state == ViewPager.SCROLL_STATE_IDLE);
                }
            }
        };

        viewPager.addOnPageChangeListener(mOnPageChangeListener);

        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setSwipeableChildren(R.id.viewpager_id,R.id.tabs);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                parseData();
                pullToRefresh.setRefreshing(false);
            }
        });


        searchText = (AutoCompleteTextView) findViewById(R.id.et_search_city_name);

//        String[] countries = getResources().getStringArray(R.array.list_of_countries);
        int resId = getResources().getIdentifier("list_of_countries", "array", this.getPackageName());
        String[] countries = getResources().getStringArray(resId);
        Log.i("AKJFK", String.valueOf(countries.length));
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,countries);
        searchText.setAdapter(adapter1);

        

        searchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

            }
        });

    }


    MultiSwipeRefreshLayout pullToRefresh;

    public void openSearchDialog()
    {
//        searchDialog.setVisibility(View.VISIBLE);
        openDialogWithAnimation(searchDialog,searchMainDialog);
        searchOpen = true;
        searchText.setText(""); //resets search field
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("21321")
                        .setMessage("432432")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
//                        locationManager.requestLocationUpdates(provider, 400, 1, this);

                        GPSTracker gps = new GPSTracker(this);
                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();

                        Log.i("latitude", String.valueOf(latitude));
                        Log.i("longitude", String.valueOf(longitude));

                        sharedpreferences.edit().putString("LAT", String.valueOf(latitude)).apply();
                        sharedpreferences.edit().putString("LON", String.valueOf(longitude)).apply();

                        parseData();
                    }

                } else {

                    //User didn't grant permission... make sure that location dialog doesn't show again
                    sharedpreferences.edit().putInt(StaticStrings.GET_DATA_FOR_FIRST_TIME_CURRENT,0).apply();
                    sharedpreferences.edit().putInt(StaticStrings.GET_DATA_FOR_FIRST_TIME_FIVE_DAY,0).apply();
                }
                return;
            }

        }
    }


    public void setCityName(String name)
    {
        cityName.setText(name);
        sharedpreferences.edit().putString(StaticStrings.CITY_TO_SEARCH, name).apply();
    }

    public void parseData()
    {
        if(isNetworkAvailable()) {

            long time= System.currentTimeMillis();
            sharedpreferences.edit().putString(StaticStrings.LAST_TIME_REFRESHED, String.valueOf(time)).apply();
            fragmentCurrentWeather.parseDataForCurrent();
            fragmentFiveDayWeather.parseDataForFiveDaysForcast();
        }
        else
        {
            Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        //Auto Refresh Data if data didn't refreshed in last 30 min
        long currentTime= System.currentTimeMillis();
        long time = Long.valueOf(sharedpreferences.getString(StaticStrings.LAST_TIME_REFRESHED, "0"));
        if(currentTime - time >= 1800000 && time != 0)
        {
            parseData();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewPager != null) {
            viewPager.removeOnPageChangeListener(mOnPageChangeListener);
        }
    }

    ///ANIMATIONS - START

    boolean isOpening = false;

    public void openDialogWithAnimation(View root, View animatedDialog)
    {
        isOpening = true;
        root.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(this, R.anim.scale_in);
        animatedDialog.startAnimation(animation);
    }


    public void closeDialogWithAnimation(final View root, View animatedDialog)
    {
        isOpening = false;
        final Handler handler = new Handler();
        animation = AnimationUtils.loadAnimation(this, R.anim.scale_out);
        animatedDialog.startAnimation(animation);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isOpening)
                    root.setVisibility(View.GONE);
            }
        }, 200);
    }

    ///ANIMATIONS  - END

    boolean doubleBackToExitPressedOnce = false;

    //BACK PRESSED SO USER MUST PRESS TWICE TO EXIT
    @Override
    public void onBackPressed() {

        if (settingsOpen) {
            closeDialogWithAnimation(settingsDialog,settingsDialogMain);
            settingsOpen = false;
        } else if (searchOpen) {
            closeDialogWithAnimation(searchDialog,searchMainDialog);
            searchOpen = false;
        } else if (firstOpen) {
            closeDialogWithAnimation(firstTimeDialog, firstDialog);
            firstOpen = false;
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }
}
