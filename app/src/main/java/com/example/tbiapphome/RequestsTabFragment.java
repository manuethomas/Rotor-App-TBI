package com.example.tbiapphome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class RequestsTabFragment extends Fragment {
    View view;
    ListView bookingsListView;
    List<Requests> bookingList;
    DeleteBookingsAdapter bookingsAdapter;
    public RequestsTabFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.requests_tab, container, false);
        bookingList = new ArrayList<>();
        bookingsListView = view.findViewById(R.id.bookingsListView);

        //fetching the user booking data from firebase
        StorageReference test = null;
        //bookingList.add(new Requests("Manu grinder",test));
        //bookingList.add(new Requests("Manu angle",test));
        //bookingsAdapter = new DeleteBookingsAdapter(getContext(), R.layout.custom_delete_bookings, bookingList);
        //bookingsListView.setAdapter(bookingsAdapter);
        //bookingsAdapter.notifyDataSetChanged();
        return view;
        //change to request tab
    }
}
