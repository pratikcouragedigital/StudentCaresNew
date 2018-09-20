package com.studentcares.spps.singleton;

import com.studentcares.spps.model.Machine_Info_Items;

import java.util.List;

public class Machine_Info_List_Instance {
    private static List<Machine_Info_Items> machineInfoList;

    public static List<Machine_Info_Items> getMachineInfoListInstance() {
        return machineInfoList;
    }

    public static void setMachineInfoListInstance(List<Machine_Info_Items> machineInfoItemsList) {
        machineInfoList = machineInfoItemsList;
    }

    private Machine_Info_List_Instance() {
    }
}
