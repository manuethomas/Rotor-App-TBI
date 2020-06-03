package com.example.tbiapphome;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.security.auth.login.LoginException;

public class MachineActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MachineAdapter adapter;
    private List<Machine> machineList;

    ProgressBar machinesProgressBar;
    static BottomNavigationView bottomNavigationView;
    int backButtonCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine);

        machinesProgressBar = findViewById(R.id.machinesProgressBar);

        machineList = new ArrayList<>();
        adapter = new MachineAdapter(this, machineList);

        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new MachineActivity.GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareMachineIcons();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.getMenu().findItem(R.id.machines).setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.requestIcon:
                        activityNavigator(RequestsActivity.class);
                        return true;
                    case R.id.machines:
                        return true;
                    case R.id.profileIcon:
                        activityNavigator(DashboardActivity.class);
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * Adding few albums for testing
     */
    private void prepareMachineIcons() {
        //Starting progressbar

        //quering the database
        //Using two array to store the name and iconurl
        final ArrayList<String> name = new ArrayList<>();
        final ArrayList<String> iconurl = new ArrayList<>();
        FirebaseDatabase mDatabase;
        DatabaseReference mRef;
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        Query machines = mRef.child("Machines");
                                                                                                        //change made here
        machines.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.clear();
                iconurl.clear();
                machineList.clear();
                adapter.notifyDataSetChanged();
                machinesProgressBar.setVisibility(View.VISIBLE);
                Log.i("info", "progressbar shown");
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    MachineDetails machineDetails = child.getValue(MachineDetails.class);
                    name.add(machineDetails.getName());
                    iconurl.add(machineDetails.getIconurl());
                }
                //items acquired in name and iconurl array, now proceeding update the machine UI
                if (name.size() > 0 && iconurl.size() > 0) {
                    for (int i = 0; i < name.size(); i++) {
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                        StorageReference iconPathReference = storageReference.child(iconurl.get(i));
                        // iconPathReference got from the iconurl
                        //updating machine UI
                        Machine a;
                        a = new Machine(name.get(i), iconPathReference);
                        machineList.add(a);
                        adapter.notifyDataSetChanged();
                    }
                    machinesProgressBar.setVisibility(View.INVISIBLE);
                    Log.i("info", "Progressbar closed");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public void activityNavigator(Class className) {
        Intent intent = new Intent(getApplicationContext(), className);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (backButtonCount >= 1){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }

    }
}
