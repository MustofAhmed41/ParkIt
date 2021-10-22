package com.example.isdmap.Models;

import java.util.ArrayList;

public class Owner {
    private String ownerName, ownerEmail, ownerPhone, password, numberOfSlots, parkingName,
    parkingAddress, costPerHour, latitude, longitude;
    private ArrayList<String> slotList;

    public Owner (){

    }

    public Owner(String ownerName, String ownerEmail, String ownerPhone, String password,
                 String numberOfSlots, String parkingName, String parkingAddress,
                 String costPerHour, String latitude, String longitude,
                 ArrayList<String> slotList) {
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.ownerPhone = ownerPhone;
        this.password = password;
        this.numberOfSlots = numberOfSlots;
        this.parkingName = parkingName;
        this.parkingAddress = parkingAddress;
        this.costPerHour = costPerHour;
        this.latitude = latitude;
        this.longitude = longitude;
        this.slotList = slotList;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNumberOfSlots() {
        return numberOfSlots;
    }

    public void setNumberOfSlots(String numberOfSlots) {
        this.numberOfSlots = numberOfSlots;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public String getParkingAddress() {
        return parkingAddress;
    }

    public void setParkingAddress(String parkingAddress) {
        this.parkingAddress = parkingAddress;
    }

    public String getCostPerHour() {
        return costPerHour;
    }

    public void setCostPerHour(String costPerHour) {
        this.costPerHour = costPerHour;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public ArrayList<String> getSlotList() {
        return slotList;
    }

    public void setSlotList(ArrayList<String> slotList) {
        this.slotList = slotList;
    }

    @Override
    public String toString() {
        return "Owner{" +
                "ownerName='" + ownerName + '\'' +
                ", ownerEmail='" + ownerEmail + '\'' +
                ", ownerPhone='" + ownerPhone + '\'' +
                ", password='" + password + '\'' +
                ", numberOfSlots='" + numberOfSlots + '\'' +
                ", parkingName='" + parkingName + '\'' +
                ", parkingAddress='" + parkingAddress + '\'' +
                ", costPerHour='" + costPerHour + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", ownerSlots=" + slotList +
                '}';
    }
}
