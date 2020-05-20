package com.example.tbiapphome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView signInTextView;
    TextView signUpTextView;
    TextView bottomWordTextView;
    Button signUpButton;
    Button googleButton;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signUpButton = findViewById(R.id.signUpButton);
        googleButton = findViewById(R.id.googleButton);
        signUpTextView = findViewById(R.id.signUpTextView);
        bottomWordTextView = findViewById(R.id.bottomWordTextView);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MachineActivity.class);
                startActivity(intent);
            }
        });
        signInTextView = findViewById(R.id.signInTextView);
        signInTextView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                    findViewById(R.id.fullNameTextView).setVisibility(View.GONE);
                    findViewById(R.id.newUnameEditText).setVisibility(View.GONE);
                    signUpTextView.setText("Sign In");
                    //now changing the bottom line components
                    bottomWordTextView.setText("New user?");
                    signInTextView.setText("Sign Up");
                    //now changing the button texts
                    signUpButton.setText("Sign In");
                    googleButton.setText("Sign In with GOOGLE");
                    return true;
            }
        });
    }

}
