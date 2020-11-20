package com.example.bbqueue;

import java.util.Date;

public class Customer {
    public String id;
    public  int partySize;
    public  String phoneNumber;
    public String name;
    public  String email;
    public String homeAddress;
    public  boolean queueStatus;
    public  String curResID;
    private Date timeEnteredQueue;


    public Customer(String i, String n, String phone, String e, String h){
        this.id = i;
        this.name = n;
        this.phoneNumber = phone;
        this.email = e;
        this.homeAddress = h;
    }

    public Customer() {};

    public Customer(String resID){
        id = "1234";
        name = "John Doe";
        phoneNumber = "1234567890";
        email = "john.doe@gmail.com";
        homeAddress = "234 street st Langley";
        queueStatus = true;
        curResID = resID;
        timeEnteredQueue = new Date();
    };


    public void setAll(String i, String n, String phone, String e, String h, int pSize, boolean qStat, String cur, Date timeQueue){
        this.id = i;
        this.name = n;
        this.phoneNumber = phone;
        this.email = e;
        this.homeAddress = h;
        partySize = pSize;
        queueStatus = qStat;
        curResID = cur;
        timeEnteredQueue = timeQueue;

    }

    //getters

    public String getId() {
        return id;
    }

    public int getPartySize() {
        return partySize;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public boolean isQueueStatus() {
        return queueStatus;
    }

    public String getCurRes() {
        return curResID;
    }

    public Date getTimeEnteredQueue() {
        return timeEnteredQueue;
    }

    //setters

    public void setId(String id) {
        this.id = id;
    }

    public void setPartySize(int partySize) {
        this.partySize = partySize;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public void setQueueStatus(boolean queueStatus) {
        this.queueStatus = queueStatus;
    }

    public void setCurRes(String curRes) {
        this.curResID = curRes;
    }

    public void setTimeEnteredQueue(Date timeEnteredQueue) {
        this.timeEnteredQueue = timeEnteredQueue;
    }
}
