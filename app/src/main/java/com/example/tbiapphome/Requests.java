package com.example.tbiapphome;

import com.google.firebase.storage.StorageReference;

public class Requests {
    String machineUsedTextView;
    String uNameTextView;
    String dateTextView;
    String uid;
    String phoneNo;
    StorageReference machineIcon;

    public Requests(String machineUsedTextView, String uNameTextView, String dateTextView, StorageReference machineIcon, String phoneNo) {
        this.machineUsedTextView = machineUsedTextView;
        this.uNameTextView = uNameTextView;
        this.dateTextView = dateTextView;
        this.machineIcon = machineIcon;
        this.phoneNo = phoneNo;
    }

    public Requests(){
        //for datasnapshot
    }

    public Requests (String machineUsedTextView, String dateTextView, String uid){
        this.machineUsedTextView = machineUsedTextView;
        this.dateTextView = dateTextView;
        this.uid = uid;
    }


    public String getMachineUsedTextView() {
        return machineUsedTextView;
    }

    public String getuNameTextView() {
        return uNameTextView;
    }

    public String getDateTextView() {
        return dateTextView;
    }

    public StorageReference getMachineIcon() {
        return machineIcon;
    }

    public String getUid() { return uid; }

    public void setUid(String uid) { this.uid = uid; }

    public String getPhoneNo() { return phoneNo; }

    public void setPhoneNo(String phoneNo) { this.phoneNo = phoneNo; }

}
