package com.example.tbiapphome;

import androidx.annotation.NonNull;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MachineActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MachineAdapter adapter;
    private List<Machine> machineList;

    static BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine);
        Log.i("info", "Its safe here");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        machineList = new ArrayList<>();
        adapter = new MachineAdapter(this, machineList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new MachineActivity.GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareMachineIcons();
        /* setting background cover
        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

         */
        // going to activity requests
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
                        activityNavigator(ProfileActivity.class);
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
        int[] covers = new int[]{
                R.drawable.m1,
                R.drawable.m2,
                R.drawable.m3,
                R.drawable.m4};
        Machine a = new Machine("Contact",covers[0]);
        machineList.add(a);

        a = new Machine("Screwdriver",covers[1]);
        machineList.add(a);

        a = new Machine("CNC ",covers[2]);
        machineList.add(a);

        a = new Machine("Drill",covers[3]);
        machineList.add(a);

        a = new Machine("CNC ",covers[2]);
        machineList.add(a);

        a = new Machine("Drill",covers[3]);
        machineList.add(a);

        a = new Machine("CNC ",covers[2]);
        machineList.add(a);

        a = new Machine("Drill",covers[3]);
        machineList.add(a);

        a = new Machine("CNC ",covers[2]);
        machineList.add(a);

        a = new Machine("Drill",covers[3]);
        machineList.add(a);

        a = new Machine("CNC ",covers[2]);
        machineList.add(a);

        a = new Machine("Drill",covers[3]);
        machineList.add(a);

        a = new Machine("Drill",covers[3]);
        machineList.add(a);

        adapter.notifyDataSetChanged();
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

    public void activityNavigator(Class className){
        Intent intent = new Intent(getApplicationContext(),className);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        machineList = new ArrayList<>();
        adapter = new MachineAdapter(this, machineList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new MachineActivity.GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareMachineIcons();
        /* setting background cover
        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

         */
        // going to activity requests
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
                        activityNavigator(ProfileActivity.class);
                        return true;
                }
                return false;
            }
        });

    }
}