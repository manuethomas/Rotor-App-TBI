package com.example.tbiapphome;

public class Requests {
    String machineUsedTextView, uNameTextView, dateTextView, TimeTextView;
    int machineUsedIconImageView;

    public Requests(String machineUsedTextView, String uNameTextView, String dateTextView, String timeTextView, int machineUsedIconImageView) {
        this.machineUsedTextView = machineUsedTextView;
        this.uNameTextView = uNameTextView;
        this.dateTextView = dateTextView;
        TimeTextView = timeTextView;
        this.machineUsedIconImageView = machineUsedIconImageView;
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

    public String getTimeTextView() {
        return TimeTextView;
    }

    public int getMachineUsedIconImageView() {
        return machineUsedIconImageView;
    }
}
