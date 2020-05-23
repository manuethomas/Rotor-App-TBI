package com.example.tbiapphome;


import android.content.Context;
import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.sapereaude.maskedEditText.MaskedEditText;

public class RequestDialog  {
    Context mcontext;
    public RequestDialog(Context context){
        mcontext = context;
    }

    public static void displayDialog(final Context activity){
        //to get the machines names , added to spinner
        final ArrayList<String> machineName = new ArrayList<>();
        machineName.add("Choose a Machine...");
        FirebaseDatabase mDatabase;
        final DatabaseReference mRef;
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mRef.keepSynced(true);
        Query query = mRef.child("Machines");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    MachineDetails machineDetails = child.getValue(MachineDetails.class);
                    machineName.add(machineDetails.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(activity, "Error fetching details", Toast.LENGTH_SHORT).show();
            }
        });
        //end of spinner values

        //going to create Alert Dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = LayoutInflater.from(activity);
        final View view = inflater.inflate(R.layout.request_dialog, null);
        final MaskedEditText dateEditText = view.findViewById(R.id.dateEditText);
        final ProgressBar requestDialogProgressBar = view.findViewById(R.id.requestDialogProgressBar);
        final TextView availabilityTextView = view.findViewById(R.id.availabilityTextView);
        requestDialogProgressBar.setVisibility(View.INVISIBLE);
        availabilityTextView.setVisibility(View.INVISIBLE);
        //final MaskedEditText startTimeEditText = view.findViewById(R.id.startTimeEditText);
        //final MaskedEditText endTimeEditText = view.findViewById(R.id.endTimeEditText);
        builder.setTitle("Create request");
        final Spinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, machineName);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final int[] availabilityCount = new int[1];
                if (position!=0){
                    requestDialogProgressBar.setVisibility(View.VISIBLE);
                    availabilityTextView.setVisibility(View.VISIBLE);
                    //if no internet connection //

                     checkConnection(activity);

                    //Querying to check if machines are available//

                    Query queryCount = mRef.child("Active Bookings");
                    queryCount.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            availabilityCount[0] = Integer.valueOf(dataSnapshot.child(spinner.getSelectedItem().toString()).child("available count").getValue().toString());
                            if (availabilityCount[0] < 1){
                                availabilityTextView.setText("Not Available");
                            }
                            else {
                                availabilityTextView.setText("Available");
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        builder.setPositiveButton("Book", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (spinner.getSelectedItem().toString().equalsIgnoreCase("")){
                    Toast.makeText(activity, "Choose a machine", Toast.LENGTH_SHORT).show();
                }
                if (dateEditText.getRawText().equalsIgnoreCase("")) {
                    Toast.makeText(activity, "Enter date", Toast.LENGTH_SHORT).show();
                }
                /*
                if (startTimeEditText.getRawText().equalsIgnoreCase("") || endTimeEditText.getRawText().equalsIgnoreCase("")){
                    Toast.makeText(activity, "Enter time", Toast.LENGTH_SHORT).show();
                }

                 */
                // If all the above parameters are satisfied then moving on to checking the request
                if (!spinner.getSelectedItem().toString().equalsIgnoreCase("") && !dateEditText.getRawText().equalsIgnoreCase("")){
                    // if none of the fields are empty then proceeding to take the spinner selected value and querying database
                    //first added the spinner value to the database


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

    public static boolean checkConnection(final Context activity){
        final int[] connectionStatus = {0};
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    connectionStatus[0] =1;

                } else {
                    Toast.makeText(activity, "Enable internet connection", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
        if (connectionStatus[0] == 1)
            return true;
        else
            return false;
    }

}


