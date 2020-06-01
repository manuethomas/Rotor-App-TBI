package com.example.tbiapphome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DashboardActivity extends AppCompatActivity {
    TextView bookingsNotificationTextView;
    TextView activeNotificationTextView;
    TextView overdueNotificationTextView;
    NonScrollListView bookingsListView;
    List<Requests> requestsList;
    TextView dashBoardUsernameTextView;
    String name = "Guest";
    String machineName;
    String date;
    String iconurl;
    String uid;
    int overdueCount=0;
    DeleteBookingsAdapter deleteBookingsAdapter;
    EditText dashboardEmailEditText;
    EditText dashboardProfessionEditText;
    EditText dashboardSemesterEditText;
    EditText dashboardBranchEditText;
    int emailTouchCount=0;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bookingsNotificationTextView = findViewById(R.id.bookingsNotificationTextView);
        activeNotificationTextView = findViewById(R.id.activeNotificationTextView);
        overdueNotificationTextView = findViewById(R.id.overdueNotificationTextView);
        dashBoardUsernameTextView = findViewById(R.id.dashBoardUsernameTextView);
        dashboardEmailEditText = findViewById(R.id.dashboardEmailEditText);
        dashboardProfessionEditText = findViewById(R.id.dashboardProfessionEditText);
        dashboardSemesterEditText = findViewById(R.id.dashboardSemesterEditText);
        dashboardBranchEditText = findViewById(R.id.dashboardBranchEditText);
        bookingsListView = findViewById(R.id.bookingsListView);
        requestsList = new ArrayList<>();
        requestsList = new ArrayList<>();

        //to check internet connection
        checkConnection(DashboardActivity.this);

        //setting email from profile info
        dashboardEmailEditText.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        //To query and get all active bookings
        FirebaseDatabase mDatabase;
        DatabaseReference mRef;
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mRef.keepSynced(true);

        //to get name, machine name and date
        try {
            Query dashboardQuery = mRef.child("Users");
            dashboardQuery.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    requestsList.clear();
                    deleteBookingsAdapter.notifyDataSetChanged();
                    try {
                        name = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").getValue().toString();
                        for (DataSnapshot child : dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Active Bookings").getChildren()) {
                            //each individual request got, taking the required values
                            machineName = child.child("machine name").getValue().toString();
                            date = child.child("date").getValue().toString();
                            iconurl = child.child("iconurl").getValue().toString();
                            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            requestsList.add(new Requests(machineName, date, uid));
                            deleteBookingsAdapter.notifyDataSetChanged();

                            //to set Username
                            dashBoardUsernameTextView.setText(name);
                            dashboardEmailEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick, 0);

                            //to check if overdue
                            String[] splitted = date.split("-");
                            if (CheckIfOverdue.checkOverdue(Integer.valueOf(splitted[0]), Integer.valueOf(splitted[1]), Integer.valueOf(splitted[2]))) {
                                overdueCount++;
                                //now upload the values to overdue in firebase
                                HashMap<String , String> overdueBookings = new HashMap<>();
                                overdueBookings.put("name", name);
                                overdueBookings.put("machine name", machineName);
                                overdueBookings.put("date", date);
                                overdueBookings.put("booking type", child.child("booking type").getValue().toString());
                                overdueBookings.put("iconurl", iconurl);
                                FirebaseDatabase.getInstance().getReference().child("Overdue").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(machineName).setValue(overdueBookings);
                            }

                        }
                        //updating the notification textviews
                        bookingsNotificationTextView.setText(String.valueOf(requestsList.size()));
                        activeNotificationTextView.setText(String.valueOf(requestsList.size()-overdueCount));
                        overdueNotificationTextView.setText(String.valueOf(overdueCount));

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("Error", "Error occured");
                    }
                    deleteBookingsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.i("info", "NO usertable found");
        }

        deleteBookingsAdapter = new DeleteBookingsAdapter(this, R.layout.custom_delete_bookings, requestsList);
        bookingsListView.setAdapter(deleteBookingsAdapter);

        //Querying firebase to get the value of emailEditText, professionEditText, branchEditText, semesterEditText;
        try {
            Query emailQuery = mRef.child("Users");
            emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).hasChild("email")) {
                        String email = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email").getValue().toString();
                        dashboardEmailEditText.setText(email);
                        dashboardEmailEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick, 0);
                    }

                    if (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).hasChild("profession")) {
                        String profession = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profession").getValue().toString();
                        dashboardProfessionEditText.setText(profession);
                        dashboardProfessionEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick, 0);
                    }

                    if (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).hasChild("semester")) {
                        String semester = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("semester").getValue().toString();
                        dashboardSemesterEditText.setText(semester);
                        dashboardSemesterEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick, 0);
                    }

                    if (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).hasChild("branch")) {
                        String branch = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("branch").getValue().toString();
                        dashboardBranchEditText.setText(branch);
                        dashboardBranchEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick, 0);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        //getting the values dashboardEmailEditText and passing it to firebase when user updates data
        dashboardEmailEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getX() >= (dashboardEmailEditText.getWidth() - dashboardEmailEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Toast.makeText(DashboardActivity.this, "Email updated", Toast.LENGTH_SHORT).show();
                        //change the drawable image to tick
                        dashboardEmailEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick, 0);
                        //adding email to firebase
                        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email").setValue(dashboardEmailEditText.getText().toString());

                        return true;
                    }
                }
                return false;
            }
        });

        dashboardProfessionEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getX() >= (dashboardProfessionEditText.getWidth() - dashboardProfessionEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Toast.makeText(DashboardActivity.this, "Profession updated", Toast.LENGTH_SHORT).show();
                        //change the drawable image to tick
                        dashboardProfessionEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick, 0);
                        //adding email to firebase
                        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profession").setValue(dashboardProfessionEditText.getText().toString());

                        return true;
                    }
                }
                return false;
            }
        });

        dashboardSemesterEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getX() >= (dashboardSemesterEditText.getWidth() - dashboardSemesterEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Toast.makeText(DashboardActivity.this, "Semester updated", Toast.LENGTH_SHORT).show();
                        //change the drawable image to tick
                        dashboardSemesterEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick, 0);
                        //adding email to firebase
                        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("semester").setValue(dashboardSemesterEditText.getText().toString());

                        return true;
                    }
                }
                return false;
            }
        });

        dashboardBranchEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getX() >= (dashboardBranchEditText.getWidth() - dashboardBranchEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Toast.makeText(DashboardActivity.this, "Branch updated", Toast.LENGTH_SHORT).show();
                        //change the drawable image to tick
                        dashboardBranchEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick, 0);
                        //adding email to firebase
                        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("branch").setValue(dashboardBranchEditText.getText().toString());

                        return true;
                    }
                }
                return false;
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
        if (connectionStatus[0] == 1)
            return true;
        else
            return false;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MachineActivity.bottomNavigationView.setSelectedItemId(R.id.machines);
    }
}
