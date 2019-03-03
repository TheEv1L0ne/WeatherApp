package com.theevilone.weatherapp;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsDialog extends DialogFragment {


    Switch celsiusCheckBox;
    Switch fahrenheitCheckBox;

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

            }
        });

        celsiusCheckBox = view.findViewById(R.id.switch1);
        fahrenheitCheckBox = view.findViewById(R.id.switch2);

        celsiusCheckBox.setChecked(true);

        celsiusCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    fahrenheitCheckBox.setChecked(false);
                }
                else
                {
                    fahrenheitCheckBox.setChecked(true);
                }
            }
        });

        fahrenheitCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    celsiusCheckBox.setChecked(false);
                }
                else
                {
                    celsiusCheckBox.setChecked(true);
                }
            }
        });

        return builder.create();
    }
}
