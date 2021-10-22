package com.example.isdmap.Models;

public class Booking {
    String ownerId, userId, startTime, endTime, duration,
            slotNumber, bookingStatus, cost,bookingId;

    public Booking(){

    }

    public Booking(String ownerId, String userId, String startTime, String endTime, String duration, String slotNumber, String bookingStatus, String cost, String bookingId) {
        this.ownerId = ownerId;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.slotNumber = slotNumber;
        this.bookingStatus = bookingStatus;
        this.cost = cost;
        this.bookingId = bookingId;
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

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }



    @Override
    public String toString() {
        return "Booking{" +
                "ownerId='" + ownerId + '\'' +
                ", userId='" + userId + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", duration='" + duration + '\'' +
                ", slotNumber='" + slotNumber + '\'' +
                ", bookingStatus='" + bookingStatus + '\'' +
                ", cost='" + cost + '\'' +
                '}';
    }
}
