package com.example.youssefwagih.servantsapplication.Business;

/**
 * Created by youssef wagih on 1/17/2016.
 */
public class Servant {
    private long ID;
    private String name;
    private String address;
    private String phoneNumber;

    public Servant(long ID, String name, String address, String phoneNumber){
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
    public Servant(String name, String address, String phoneNumber){
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
    public Servant(){}

    public Long getID() {
        return ID;
    }
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

