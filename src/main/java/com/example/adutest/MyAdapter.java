package com.example.adutest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MyAdapter {
    Date day = new Date();
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    String toDay = date.format(day);

    String sendtxt;
    ArrayList<Data> sendList = new ArrayList<Data>();

    public  MyAdapter(String sendtxt){
        this.sendtxt = sendtxt;
    }
    public void addItem(Data d){
        sendList.add(d);
    }

    public ArrayList<Data> getStudents(){
        return sendList;
    }

    public void setStudents(ArrayList<Data> sendList) {
        this.sendList = sendList;
    }

    public String getSendtxt() {
        return sendtxt;
    }

    public void setSendtxt(String sendtxt) {
        this.sendtxt = sendtxt;
    }
    public int size(){
        return sendList.size();
    }
    public String toString(){
        String output= sendtxt+"\n";

//        for (int i=0; i<sendList.size(); i++){
        for (int i=sendList.size()-1; 0 <= i; i--){

           Data data = sendList.get(i);

            output +=toDay+" \t"+ data.getSendtxt() +"\n";

        }
        return output;
    }

}
