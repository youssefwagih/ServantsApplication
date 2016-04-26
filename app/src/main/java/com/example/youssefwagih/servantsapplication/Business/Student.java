package com.example.youssefwagih.servantsapplication.Business;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by youssef wagih on 1/17/2016.
 */
public class Student {
    private long ID;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private LatLng location;

    public Student(long ID, String name, String address, String phoneNumber, LatLng location, String email){
        this.ID = ID;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.location = location;
    }
    public Student(String name, String address, String phoneNumber, LatLng location, String email){
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.location = location;
    }
    public Student(){}

    public Long getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LatLng getLocation(){return location;}

    public void setID(long ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setLocation(LatLng location){ this.location = location; }

    @Override
    public String toString() {
        return "Name: " + getName() + "\n" + "Address: " + getAddress() + "\n" + "Phone Number:" + getPhoneNumber();
    }
}
