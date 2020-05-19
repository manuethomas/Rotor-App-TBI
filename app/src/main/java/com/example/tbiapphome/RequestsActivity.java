package com.example.tbiapphome;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class RequestsActivity extends AppCompatActivity {
    List<Requests> requestsList;
    ListView requestsListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        requestsList = new ArrayList<Requests>();
        requestsList.add(new Requests("Screwdriver", "Manu E Thomas", "04 July", "9:30 - 12:30", R.drawable.m2));
        requestsList.add(new Requests("Contact", "Maneeta", "04 April", "2:30 - 12:30", R.drawable.m1));
        requestsList.add(new Requests("CNC Milling Machine", "Appa", "04 December", "11:30 - 12:30", R.drawable.m3));
        requestsList.add(new Requests("Drill Machine", "Amma", "20 July", "9:30 - 12:30", R.drawable.m4));
        requestsListView = findViewById(R.id.requestsListView);
        RequestsAdapter requestsAdapter = new RequestsAdapter(this, R.layout.custom_requests, requestsList);
        requestsListView.setAdapter(requestsAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MachineActivity.bottomNavigationView.setSelectedItemId(R.id.machines);
    }
}
