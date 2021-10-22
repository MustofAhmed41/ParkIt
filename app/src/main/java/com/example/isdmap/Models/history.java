package com.example.isdmap.Models;

public class history {
    private String cost;
    private String bookingStatus;
    private String startTime;
    private String endTime;
    private String duration;
    private String slotNumber;
    private String ownerId;
    private String userId;
    public history(){}

    public history(String cost, String bookingStatus, String startTime, String endTime, String duration, String slotNumber, String ownerId, String userId) {
        this.cost = cost;
        this.bookingStatus = bookingStatus;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.slotNumber = slotNumber;
        this.ownerId = ownerId;
        this.userId = userId;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(String slotNumber) {
        this.slotNumber = slotNumber;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
