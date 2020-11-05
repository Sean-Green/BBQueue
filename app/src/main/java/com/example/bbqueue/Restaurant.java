package com.example.bbqueue;

import java.util.ArrayList;

public class Restaurant {
    private  String resID;
    private int avgwait;
    private Section sections;
    private ArrayList<Customer> queue;
    public  String name;
    public  String address;
    public  String phoneNumber;

    public Restaurant(String n, String a, String phoneNum, String res){
        name = n;
        address = a;
        phoneNumber = phoneNum;
        resID = res;
        sections = new Section();
        queue = new ArrayList<Customer>();
        avgwait = 30;
    }

    public Restaurant(){}

    public String getResID() {
        return resID;
    }

    public void setResID(String resID) {
        this.resID = resID;
    }

    public int getAvgwait() {
        return avgwait;
    }

    public void setAvgwait(int avgwait) {
        this.avgwait = avgwait;
    }

    public Section getSections() {
        return sections;
    }

    public void setSections(Section sections) {
        this.sections = sections;
    }

    public ArrayList<Customer> getQueue() {
        return queue;
    }

    public void setQueue(ArrayList<Customer> queue) {
        this.queue = queue;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    private static String url;
    public String getUrl(){
        return url;
    }
    public void setUrl(String url){
        this.url = url;
    }


}
