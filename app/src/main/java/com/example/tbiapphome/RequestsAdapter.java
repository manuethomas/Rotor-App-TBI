package com.example.tbiapphome;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

public class RequestsAdapter extends ArrayAdapter<Requests> {
    Context context;
    int resource;
    List<Requests> requestsList;
    public RequestsAdapter(@NonNull Context context, int resource, @NonNull List<Requests> requestsList) {
        super(context, resource, requestsList);
        this.context = context;
        this.resource = resource;
        this.requestsList= requestsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_requests ,null);
        TextView machineUsedTextView = view.findViewById(R.id.machineUsedTextView);
        TextView uNameTextView = view.findViewById(R.id.uNameTextView);
        TextView dateTextView = view.findViewById(R.id.dateTextView);
        TextView phoneNoTextView = view.findViewById(R.id.phoneNoTextView);
        ImageView machineUsedIconImageView = view.findViewById(R.id.machineUsedIconImageView);
        Requests requests = requestsList.get(position);
        machineUsedTextView.setText(requests.getMachineUsedTextView());
        dateTextView.setText(requests.getDateTextView());
        phoneNoTextView.setText(requests.getPhoneNo());
        uNameTextView.setText(requests.getuNameTextView());
        GlideApp.with(context).load(requests.getMachineIcon()).placeholder(R.drawable.blankimg).into(machineUsedIconImageView);
        Log.i("getMachineIcon", requests.getMachineIcon().toString());
        return view;

    }
}
