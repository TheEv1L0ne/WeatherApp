package com.theevilone.weatherapp;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.theevilone.weatherapp.HelperClasses.StaticStrings;

public class SettingsDialog extends DialogFragment {

    private SharedPreferences sharedpreferences;

    private Switch celsiusCheckBox;
    private Switch fahrenheitCheckBox;

    private MainActivity activity;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        final AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.unit_dialog,null);
        builder.setView(view);
        builder.setTitle("Settings");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.parseData();
            }
        });


        sharedpreferences = MainActivity.staticMainActivity.getSharedPreferences(StaticStrings.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        celsiusCheckBox = view.findViewById(R.id.switch1);
        fahrenheitCheckBox = view.findViewById(R.id.switch2);

        // 0 - Celsius selected
        // 1 - Fahrenheit selected


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

        return builder.create();
    }

    public void SetActivity(MainActivity activity)
    {
        this.activity = activity;
    }
}
