package com.example.tbiapphome;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import br.com.sapereaude.maskedEditText.MaskedEditText;

public class RequestDialog  {
    Context mcontext;
    String selectedMachineName;
    public RequestDialog(Context context, String selectedMachineName){
        mcontext = context;
        this.selectedMachineName = selectedMachineName;
    }

    @SuppressLint("ClickableViewAccessibility")
    public static void displayDialog(final Context activity, final String selectedMachineName){
        //not used now
        /*
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

         */

        //going to create Alert Dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = LayoutInflater.from(activity);
        final View view = inflater.inflate(R.layout.request_dialog, null);
        //date feature yet to be added
        //final MaskedEditText dateEditText = view.findViewById(R.id.dateEditText);
        final LinearLayout progressBarLinLayout = view.findViewById(R.id.progressBarLinLayout);
        progressBarLinLayout.setVisibility(View.GONE);
        final ProgressBar requestDialogProgressBar = view.findViewById(R.id.requestDialogProgressBar);
        final TextView availabilityTextView = view.findViewById(R.id.availabilityTextView);
        final EditText fullNameEditText = view.findViewById(R.id.fullNameEditText);
        final MaskedEditText phoneEditText = view.findViewById(R.id.phoneEditText);
        LinearLayout requestDialogLinearLayout = view.findViewById(R.id.requestDialogLinearLayout);
        TextView machineSelectedTextView = view.findViewById(R.id.machineSelectedTextView);
        final int[] availabilityCount = new int[1];
        builder.setTitle("Create booking");
        //spinner not used now
        /*
        final Spinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, machineName);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

         */

        machineSelectedTextView.setText(selectedMachineName);
        //displaying the availability status and progressbar
        progressBarLinLayout.setVisibility(View.VISIBLE);
        //if no internet connection //
        checkConnection(activity);
        //Querying to check if machines are available//
        FirebaseDatabase mDatabase;
        final DatabaseReference mRef;
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mRef.keepSynced(true);
        Query queryCount = mRef.child("Active Bookings");
        queryCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                availabilityCount[0] = Integer.valueOf(dataSnapshot.child(selectedMachineName).child("available count").getValue().toString());
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


        //setting touch listener for requestDialogLinearLayout
        requestDialogLinearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                return true;
            }
        });

        //setting listener for spinner//
        /*
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0){
                    progressBarLinLayout.setVisibility(View.VISIBLE);

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
        //Spinner listener ended//

         */

        //setting positive button//
        builder.setPositiveButton("Book", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                Boolean flag = true;
                if (selectedMachineName.equalsIgnoreCase("Choose a Machine...")){
                    flag = false;
                    Toast.makeText(activity, "Choose a machine", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                if (fullNameEditText.getText().toString().equalsIgnoreCase("")){
                    flag = false;
                    Toast.makeText(activity, "Enter name", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                if (phoneEditText.getRawText().equalsIgnoreCase("") || phoneEditText.getRawText().length()<10){
                    flag = false;
                    Toast.makeText(activity, "Enter a valid phone number", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                /*
                Date feature yet to be added
                if (dateEditText.getRawText().equalsIgnoreCase("")){
                    flag = false;
                    Toast.makeText(activity, "Enter a valid date", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                else{
                    //Checking if the date entered is valid
                    try {
                        if (!isEnteredDateValid(dateEditText.getText().toString(), activity))
                            flag = false;
                    } catch (ParseException e) {
                        flag = false;
                        e.printStackTrace();
                        dialog.dismiss();
                    }
                }

                 */
                if (availabilityCount[0] < 1 && !selectedMachineName.equalsIgnoreCase("Choose a Machine...")){
                    Toast.makeText(activity, "No available machines", Toast.LENGTH_SHORT).show();
                    flag = false;
                }

                // If all the above parameters are satisfied then moving on to querying database
                if (flag){
                    //means now machines are available, then we are good to go. Proceeding to booking
                    //getting machineIcon and uploading data to Active Bookings
                    final String[] iconurl = new String[1];
                    Query machineIcon = mRef.child("Machines");
                    machineIcon.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            iconurl[0] = dataSnapshot.child(selectedMachineName).child("iconurl").getValue().toString();
                            //getting current date first
                            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                            Calendar calendar = Calendar.getInstance();
                            String currentDate = df.format(calendar.getTime());

                            //creating a hashmap
                            HashMap<String, Object> bookingInfo = new HashMap<>();
                            bookingInfo.put("booking type", "Start");
                            bookingInfo.put("name", fullNameEditText.getText().toString());
                            bookingInfo.put("machine name", selectedMachineName);
                            bookingInfo.put("date", currentDate);
                            bookingInfo.put("iconurl", iconurl[0]);
                            bookingInfo.put("phone number", phoneEditText.getRawText());
                            bookingInfo.put("timestamp", ServerValue.TIMESTAMP);

                            String key = FirebaseDatabase.getInstance().getReference().push().getKey();
                            FirebaseDatabase.getInstance().getReference().child("Active Bookings").child(selectedMachineName).child("Bookings").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key).setValue(bookingInfo);
                            FirebaseDatabase.getInstance().getReference().child("Active Bookings").child(selectedMachineName).child("available count").setValue(availabilityCount[0]-1);
                            Toast.makeText(activity, "Booked", Toast.LENGTH_SHORT).show();

                            //moving one copy to All Bookings and Users
                            //getting unique key for all bookings
                            FirebaseDatabase.getInstance().getReference().child("All Bookings").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(selectedMachineName).child(key).setValue(bookingInfo);
                            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Active Bookings").child(selectedMachineName).child(key).setValue(bookingInfo);

                            //adding name to users
                            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").setValue(fullNameEditText.getText().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        //setting negative button
        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        //if no internet dismiss the dialog. only if there is internet the dialog appears.
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {

                } else {
                    Toast.makeText(activity, "Check internet connection", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
    }

    public static boolean checkConnection(final Context activity){
        final int[] connectionStatus = {0};
        int delay = 2000; //msecs
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            public void run()
            {
                // put the code you want to run here
                // It will get executed in 2000 msecs
                DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
                connectedRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        boolean connected = snapshot.getValue(Boolean.class);
                        if (connected) {
                            connectionStatus[0] =1;

                        } else {
                            Toast.makeText(activity, "Check internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        System.err.println("Listener was cancelled");
                    }
                });
            }

        }, delay);
        return connectionStatus[0] == 1;

    }

    public static boolean isEnteredDateValid(String enteredDate, Context activity) throws ParseException {
        //getting current date
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        String currentDate = df.format(calendar.getTime());
        //next checking if current date is valid//
        String[] splitted = enteredDate.split("-");
        try {
            //try catch used to catch exception when the date value is partially filled
            if (CheckIfValidDate.isValidDate(Integer.valueOf(splitted[0]), Integer.valueOf(splitted[1]), Integer.valueOf(splitted[2]))){
                //now comparing current date and entered date;
                if (CompareTwoDatesTest.compareTwoDates(enteredDate, currentDate)){
                    return true;
                }
            }
        }catch (Exception e){
            Toast.makeText(activity, "Enter a valid date", Toast.LENGTH_SHORT).show();
            return false;
        }
        Toast.makeText(activity, "Enter a valid date", Toast.LENGTH_SHORT).show();
        return false;
    }

}


