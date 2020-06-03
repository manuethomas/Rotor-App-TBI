package com.example.tbiapphome;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import br.com.sapereaude.maskedEditText.MaskedEditText;

public class MachineAdapter extends RecyclerView.Adapter<MachineAdapter.MyViewHolder>  {

    private Context mContext;
    private List<Machine> machineList;
    //Variable for the

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView machineCardName;
        public ImageView overflow, machineCardImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            machineCardName = itemView.findViewById(R.id.machineCardName);
            machineCardImageView = itemView.findViewById(R.id.machineCardImageView);
            overflow = itemView.findViewById(R.id.overflow);
        }

    }//myViewHolder class ended

    public MachineAdapter(Context mContext, List<Machine> machineList) {
        this.mContext = mContext;
        this.machineList = machineList;
    }

    @NonNull
    @Override
    public MachineAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.machine_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MachineAdapter.MyViewHolder holder, int position) {
        Machine machine = machineList.get(position);
        holder.machineCardName.setText(machine.getMachineName());

        // loading machine icons using Glide library
        Glide.with(mContext).load(machine.getMachineIcon()).into(holder.machineCardImageView);
        //Log.i("MachineAdapter", machine.getMachineIcon().toString());

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });

    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_machine, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }


    /**
     * Click listener for popup menu items
     */

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.book:
                    RequestDialog.displayDialog(mContext);
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return machineList.size();
    }

}
