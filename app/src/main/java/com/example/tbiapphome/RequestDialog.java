package com.example.tbiapphome;


import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import br.com.sapereaude.maskedEditText.MaskedEditText;

public class RequestDialog  {
    Context mcontext;
    public RequestDialog(Context context){
        mcontext = context;
    }

    public static void displayDialog(final Context activity){
        final String[] machinesArray = {"Choose a Machine...", "CNC Machine", "Drill Machine", "Screwdriver", "Contact"," Demo1", "Demo2", "Demo3", "Demo4", "Demo5"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = LayoutInflater.from(activity);
        final View view = inflater.inflate(R.layout.request_dialog, null);
        final MaskedEditText dateEditText = view.findViewById(R.id.dateEditText);
        final MaskedEditText startTimeEditText = view.findViewById(R.id.startTimeEditText);
        final MaskedEditText endTimeEditText = view.findViewById(R.id.endTimeEditText);
        builder.setTitle("Create request");
        final Spinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, machinesArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        builder.setPositiveButton("Book", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (spinner.getSelectedItem().toString().equalsIgnoreCase("")){
                    Toast.makeText(activity, "Choose a machine", Toast.LENGTH_SHORT).show();
                }
                if (dateEditText.getRawText().equalsIgnoreCase("")) {
                    Toast.makeText(activity, "Enter date", Toast.LENGTH_SHORT).show();
                }
                if (startTimeEditText.getRawText().equalsIgnoreCase("") || endTimeEditText.getRawText().equalsIgnoreCase("")){
                    Toast.makeText(activity, "Enter time", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}


