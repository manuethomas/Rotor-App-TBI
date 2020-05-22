package com.example.tbiapphome;

    /*
    new class for getting machine name and iconurl from firebase
     */

public class MachineDetails{
    public String iconurl;
    public String name;

    public MachineDetails(String iconurl, String name) {
        this.iconurl = iconurl;
        this.name = name;
    }

    public MachineDetails(){

    }

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}