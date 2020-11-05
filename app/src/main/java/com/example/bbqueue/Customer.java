package com.example.bbqueue;

public class Customer {
    public String id;
    public  int partySize;
    public  String phoneNumber;
    public String name;
    public  String email;
    public String homeAddress;
    public  boolean queueStatus;
    public  Restaurant curRes = null;
    private int timeEnteredQueue;


    public Customer(String i, String n, String phone, String e, String h){
        this.id = i;
        this.name = n;
        this.phoneNumber = phone;
        this.email = e;
        this.homeAddress = h;

    }

    public Customer(){};


    public void setAll(String i, String n, String phone, String e, String h, int pSize, boolean qStat, Restaurant cur, int timeQueue){
        this.id = i;
        this.name = n;
        this.phoneNumber = phone;
        this.email = e;
        this.homeAddress = h;
        partySize = pSize;
        queueStatus = qStat;
        curRes = cur;
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

    public Restaurant getCurRes() {
        return curRes;
    }

    public int getTimeEnteredQueue() {
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

    public void setCurRes(Restaurant curRes) {
        this.curRes = curRes;
    }

    public void setTimeEnteredQueue(int timeEnteredQueue) {
        this.timeEnteredQueue = timeEnteredQueue;
    }
}
