package com.example.tbiapphome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RequestsActivity extends AppCompatActivity {
    List<Requests> requestsList;
    ListView requestsListView;
    String machineName;
    String name;
    String date;
    String iconurl;
    String phoneNo;
    String uid;
    RequestsAdapter requestsAdapter;
    int checkConnectionCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        requestsList = new ArrayList<Requests>();
        final ProgressBar requestActivityProgressBar = findViewById(R.id.requestActivityProgressBar);
        requestActivityProgressBar.setVisibility(View.INVISIBLE);

        //to check internet connection
        if (checkConnectionCount<2) {
            checkConnection(this);
            checkConnectionCount++;
        }

        //querying database to get all the requests
        FirebaseDatabase mDatabase;
        final DatabaseReference mRef;
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mRef.keepSynced(true);

        //to get name, date and machineName and machineicon
        Query queryActiveBookings = mRef.child("Active Bookings");
        queryActiveBookings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestsList.clear();
                requestsAdapter.notifyDataSetChanged();
                requestActivityProgressBar.setVisibility(View.VISIBLE);
                for (DataSnapshot individualMachines : dataSnapshot.getChildren()){
                    try {
                        for (DataSnapshot uids : individualMachines.child("Bookings").getChildren()){
                            for (DataSnapshot individualBookings: uids.getChildren()) {
                                name = individualBookings.child("name").getValue().toString();
                                date = individualBookings.child("date").getValue().toString();
                                machineName = individualBookings.child("machine name").getValue().toString();
                                iconurl = individualBookings.child("iconurl").getValue().toString();
                                phoneNo = individualBookings.child("phone number").getValue().toString();
                                uid = uids.getKey();
                                Log.i("info", name);
                                Log.i("info", date);
                                Log.i("info", machineName);
                                Log.i("info", iconurl);

                                //to get pathReference of machineIcon from iconurl
                                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                                StorageReference iconPathReference = storageReference.child(iconurl);
                                requestsList.add(new Requests(machineName, name, date, iconPathReference, phoneNo));
                                requestsAdapter.notifyDataSetChanged();
                                requestActivityProgressBar.setVisibility(View.INVISIBLE);

                                //to check if overdue
                                String[] splitted = date.split("-");
                                if (CheckIfOverdue.checkOverdue(Integer.valueOf(splitted[0]), Integer.valueOf(splitted[1]), Integer.valueOf(splitted[2]))) {
                                    Log.i("info", " i am overdue");
                                    //now upload the values to overdue in firebase
                                    HashMap<String, String> overdueBookings = new HashMap<>();
                                    overdueBookings.put("name", name);
                                    overdueBookings.put("machine name", machineName);
                                    overdueBookings.put("date", date);
                                    overdueBookings.put("booking type", individualBookings.child("booking type").getValue().toString());
                                    overdueBookings.put("iconurl", iconurl);
                                    overdueBookings.put("date", date);
                                    overdueBookings.put("phone number", phoneNo);
                                    FirebaseDatabase.getInstance().getReference().child("Overdue").child(uid).child(machineName).child(individualBookings.getKey()).setValue(overdueBookings);
                                }
                            }


                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        requestsListView = findViewById(R.id.requestsListView);
        requestsAdapter = new RequestsAdapter(this, R.layout.custom_requests, requestsList);
        requestsListView.setAdapter(requestsAdapter);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MachineActivity.bottomNavigationView.setSelectedItemId(R.id.machines);
    }
}
