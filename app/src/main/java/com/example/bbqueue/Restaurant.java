package com.example.bbqueue;

import java.util.ArrayList;

public class Restaurant {
    private String url;
    private String resID;
    private int avgwait;
    private ArrayList<Section> sections;
    private ArrayList<Customer> waitList;
    public  String name;
    public  String address;
    public  String phoneNumber;

    public Restaurant(String n, String a, String phoneNum, String res){
        name = n;
        address = a;
        phoneNumber = phoneNum;
        resID = res;
        sections = new ArrayList<Section>();
        sections.add(new Section());
        waitList = new ArrayList<Customer>();
        waitList.add(new Customer(this.resID));
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

    public ArrayList<Section> getSections() {
        return sections;
    }

    public void setSections(ArrayList<Section> sections) {
        this.sections = sections;
    }

    public ArrayList<Customer> getWaitList() {
        return waitList;
    }

    public void setWaitList(ArrayList<Customer> waitList) {
        this.waitList = waitList;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }


    public void addSection() {
        sections.add(new Section());
    }

    public ArrayList<Table> getSectionTables(int index){
        return sections.get(index).getTables();
    }

    public Section getSectionIndex(int i) {
        return sections.get(i);
    }
}
