package com.example.tbiapphome;

public class Machine {
    String machineName;
    int machineIcon;

    public Machine(String machineName, int machineIcon) {
        this.machineName = machineName;
        this.machineIcon = machineIcon;
    }

    public String getMachineName() {
        return machineName;
    }

    public int getMachineIcon() {
        return machineIcon;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public void setMachineIcon(int machineIcon) {
        this.machineIcon = machineIcon;
    }
}
