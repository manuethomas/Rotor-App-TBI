package com.example.tbiapphome;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class DeleteBookingsAdapter extends ArrayAdapter<Requests> {
    Context context;
    int resource;
    List<Requests> requestsList;
    public DeleteBookingsAdapter(@NonNull Context context, int resource, @NonNull List<Requests> requestsList) {
        super(context, resource, requestsList);
        this.context = context;
        this.resource = resource;
        this.requestsList= requestsList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_delete_bookings ,null);
        final TextView machineNameTextView = view.findViewById(R.id.machineNameTextView);
        TextView dateTextView = view.findViewById(R.id.dateTextView);
        final Requests requests = requestsList.get(position);
        machineNameTextView.setText(requests.getMachineUsedTextView());
        dateTextView.setText(requests.getDateTextView());
        view.findViewById(R.id.endBookingButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code
                Log.i("info", "Button Clicked");
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Confirm");
                dialog.setMessage("Are you sure you want to end the booking");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //first adding the delete log to all bookings table
                        //getting current date first
                        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        Calendar calendar = Calendar.getInstance();
                        String currentDate = df.format(calendar.getTime());

                        HashMap<String, Object> bookingInfo = new HashMap<>();
                        bookingInfo.put("booking type", "End");
                        bookingInfo.put("name", requests.getuNameTextView());
                        bookingInfo.put("machine name", requests.getMachineUsedTextView());
                        bookingInfo.put("date",currentDate);
                        bookingInfo.put("phone number", requests.getPhoneNo());
                        bookingInfo.put("timestamp", ServerValue.TIMESTAMP);

                        String key = FirebaseDatabase.getInstance().getReference().push().getKey();
                        FirebaseDatabase.getInstance().getReference().child("All Bookings").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(requests.getMachineUsedTextView()).child(key).setValue(bookingInfo);

                        requestsList.remove(position);
                        notifyDataSetChanged();
                        dialog.dismiss();

                        //removing the booking from active bookings
                        FirebaseDatabase.getInstance().getReference().child("Active Bookings").child(requests.getMachineUsedTextView()).child("Bookings").removeValue();

                        //removing the booking from Users
                        FirebaseDatabase.getInstance().getReference().child("Users").child(requests.getUid()).child("Active Bookings").child(requests.getMachineUsedTextView()).removeValue();

                        //removing from overdue if any
                        try {
                            String[] splitted = requests.getDateTextView().split("-");
                            if (CheckIfOverdue.checkOverdue(Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2]))) {
                                FirebaseDatabase.getInstance().getReference().child("Overdue").child(requests.getUid()).child(requests.getMachineUsedTextView()).removeValue();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        //now updating the machine count
                        //quering to get the present available count
                        FirebaseDatabase mDatabase;
                        DatabaseReference mRef;
                        mDatabase = FirebaseDatabase.getInstance();
                        mRef = mDatabase.getReference();
                        mRef.keepSynced(true);
                        final int[] availabilityCount = new int[1];
                        Query availableCountQuery = mRef.child("Active Bookings");
                        availableCountQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                availabilityCount[0] = Integer.valueOf(dataSnapshot.child(requests.getMachineUsedTextView()).child("available count").getValue().toString());
                                //setting the new value of available count
                                FirebaseDatabase.getInstance().getReference().child("Active Bookings").child(requests.getMachineUsedTextView()).child("available count").setValue(availabilityCount[0]+1);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        return view;
    }
}
