package com.theevilone.weatherapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class ChooseCityDialog  extends DialogFragment {


    EditText searchCityName;
    MainActivity activity;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.choose_city_dialog, null);
        builder.setView(view);
        builder.setTitle("Search city");
        builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(searchCityName.getText().toString().isEmpty())
                {
                    Toast.makeText(getActivity(), "Please enter city name!",
                            Toast.LENGTH_LONG).show();
                }

                activity.parseData();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        searchCityName = view.findViewById(R.id.et_search_city_name);

        return builder.create();
    }

    public void SetActivity(MainActivity activity)
    {
        this.activity = activity;
    }

}