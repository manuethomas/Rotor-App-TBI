package com.example.tbiapphome;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class GlideTestingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide_testing);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        ImageView imageView2 = findViewById(R.id.imageView2);
        StorageReference pathReference = storageReference.child("machine images/m2.jpg");
        Log.i("path", pathReference.toString());
        Glide.with(this /* context */)
                .load(pathReference)
                .into(imageView2);

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query machines = mDatabase.child("Machines");
        machines.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String name = dataSnapshot.child("name").getValue().toString();
                Log.i("info", name);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    }
}
