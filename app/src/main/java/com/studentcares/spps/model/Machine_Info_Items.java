package com.studentcares.spps.model;

public class Machine_Info_Items {

    public String Machine_No;
    public String Status;


    public Machine_Info_Items() {
    }

    public Machine_Info_Items(String Machine_No, String Status) {

        this.Machine_No = Machine_No;
        this.Status = Status;
    }

    public String getMachine_No() {
        return Machine_No;
    }
    public void setMachine_No(String Machine_No) {
        this.Machine_No = Machine_No;
    }


    public String getStatus() {
        return Status;
    }
    public void setStatus(String Status) {
        this.Status = Status;
    }
}
