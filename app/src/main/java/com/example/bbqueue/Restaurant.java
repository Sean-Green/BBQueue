package com.example.bbqueue;
public class Restaurant {
    private String name;
    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    private String url;
    public String getUrl(){return url;}
    public void setUrl(String url){this.url = url;}

    private String imgurl;
    public String getImgurl(){return imgurl;}
    public void setImgurl(String imgurl){this.imgurl = imgurl;}

    private int wait_time;
    public int getWait_time(){return wait_time;}
    public void setWait_time(int wait_time){this.wait_time = wait_time;}
    //add more when needed

    public Restaurant(String name, String url, String imgurl, int wait_time){
        this.name = name;
        this.url = url;
        this.imgurl = url;
        this.wait_time = wait_time;
    }
    public Restaurant(){}
}
