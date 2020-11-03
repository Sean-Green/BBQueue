package com.example.bbqueue;

import java.util.ArrayList;

public class Restaurant {
    private String resID;
    private int avgwait;
    private ArrayList<Section> sections;
    private Queue queue;
    private String name;

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

    public ArrayList<Section> getSections() {
        return sections;
    }

    public void setSections(ArrayList<Section> sections) {
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

    private String url;
    public String getUrl(){
        return url;
    }
    public void setUrl(String url){
        this.url = url;
    }

    public Restaurant(String name, String url, String resID){
        this.name = name;
        this.url = url;
        this.resID = resID;
    }
    public Restaurant(){}
}
