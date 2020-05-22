package com.example.tbiapphome;

import com.google.firebase.storage.StorageReference;

public class Machine {
    String machineName;
    StorageReference machineIcon;

    public Machine(String machineName, StorageReference machineIcon) {
        this.machineName = machineName;
        this.machineIcon = machineIcon;
    }

    public String getMachineName() {
        return machineName;
    }

    public StorageReference getMachineIcon() {
        return machineIcon;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public void setMachineIcon(StorageReference machineIcon) {
        this.machineIcon = machineIcon;
    }
}
