package com.example.tbiapphome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
        TextView timeTextView = view.findViewById(R.id.timeTextView);
        ImageView machineUsedIconImageView = view.findViewById(R.id.machineUsedIconImageView);
        Requests requests = requestsList.get(position);
        machineUsedTextView.setText(requests.getMachineUsedTextView());
        dateTextView.setText(requests.getDateTextView());
        uNameTextView.setText(requests.getuNameTextView());
        timeTextView.setText(requests.getTimeTextView());
        machineUsedIconImageView.setImageDrawable(context.getDrawable(requests.getMachineUsedIconImageView()));
        return view;

    }
}
