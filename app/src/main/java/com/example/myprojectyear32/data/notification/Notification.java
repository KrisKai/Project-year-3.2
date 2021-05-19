package com.example.myprojectyear32.data.notification;


public class Notification{
    private String mDescription;
    private String mTime;
    private int mImage;

    public Notification(){

    }

    public void setDescription(String Description){
        this.mDescription = Description;
    }

    public void setTime(String Time){
        this.mTime = Time;
    }

    public void setImage(Integer Image){ this.mImage = Image;}

    public String getDescription(){
        return mDescription;
    }

    public String getTime(){
        return mTime;
    }

    public int getImage() {
        return mImage;
    }

}
