package com.example.tbiapphome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;

public class ProfileActivity extends AppCompatActivity {
    AvatarView avatarView;
    IImageLoader imageLoader;
    TabLayout tableLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        avatarView = (AvatarView) findViewById(R.id.avatar_view_example);

        viewPager = findViewById(R.id.viewPager);
        tableLayout = findViewById(R.id.tabLayout);

        imageLoader = new PicassoLoader();
        imageLoader.loadImage(avatarView, "http:/example.com/user/someUserAvatar.png", "Manu");

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new AboutTabFragment(), "ABOUT");
        viewPagerAdapter.addFragment(new RequestsTabFragment(), "REQUEST");
        viewPager.setAdapter(viewPagerAdapter);
        tableLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.bottomNavigationView.setSelectedItemId(R.id.machines);
    }
}
