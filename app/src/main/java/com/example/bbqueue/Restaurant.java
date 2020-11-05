package com.example.bbqueue;

import java.util.ArrayList;

public class Restaurant {
    private  String resID;
    private int avgwait;
    private Section sections;
    private  Queue queue;
    public  String name;
    public  String address;
    public  String phoneNumber;

    public Restaurant(String n, String a, String phoneNum, String res){
        name = n;
        address = a;
        phoneNumber = phoneNum;
        resID = res;
        sections = new Section();
        queue  = null;
        avgwait = 0;
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

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
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
