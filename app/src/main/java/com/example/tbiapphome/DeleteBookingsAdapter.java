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
import android.widget.Toast;

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
import java.util.Timer;
import java.util.TimerTask;

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
        final ImageView translucentImageView = view.findViewById(R.id.traslucentImageView);
        final TextView sendTextView = view.findViewById(R.id.sendTextView);
        final Requests requests = requestsList.get(position);
        machineNameTextView.setText(requests.getMachineUsedTextView());
        dateTextView.setText(requests.getDateTextView());

        Log.i("Info",requests.getBookingType());
        //if booking type equals request send then display the translucent imageview
        if (requests.getBookingType().equalsIgnoreCase("request send")){
            translucentImageView.setAlpha((float) 0.92);
            sendTextView.setAlpha(1);
            view.findViewById(R.id.endBookingButton).setEnabled(false);
        }
        view.findViewById(R.id.endBookingButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to end the booking");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //code

                        // displaying the translucentImageView and disabling the end button
                            translucentImageView.setAlpha((float) 0.92);
                            sendTextView.setAlpha(1);

                        //getting current date first
                        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        Calendar calendar = Calendar.getInstance();
                        String currentDate = df.format(calendar.getTime());

                        // send the end request to admin
                        HashMap<String, Object> bookingInfo = new HashMap<>();
                        bookingInfo.put("booking type", "End");
                        bookingInfo.put("name", requests.getuNameTextView());
                        bookingInfo.put("machine name", requests.getMachineUsedTextView());
                        bookingInfo.put("date",currentDate);
                        bookingInfo.put("phone number", requests.getPhoneNo());
                        bookingInfo.put("timestamp", ServerValue.TIMESTAMP);

                        FirebaseDatabase.getInstance().getReference().child("Admin").child("End Requests").child(FirebaseAuth.getInstance().getUid()).child(requests.getMachineUsedTextView()).child(requests.getKey()).setValue(bookingInfo);

                        //changing the booking type to 'request sent'
                        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("Active Bookings").child(requests.getMachineUsedTextView()).child(requests.getKey()).child("booking type").setValue("request send");
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();


            }
        });
        return view;
    }

}
