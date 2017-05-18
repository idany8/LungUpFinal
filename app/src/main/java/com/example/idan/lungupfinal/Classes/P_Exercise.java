package com.example.idan.lungupfinal.Classes;

import com.example.idan.lungupfinal.Classes.Exercise;
import com.example.idan.lungupfinal.Classes.PerfUnit;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Idan on 17/04/2017.
 */

public class P_Exercise extends Exercise implements Serializable {

    private String schedule;
    private ArrayList<PerfUnit> records = new ArrayList<PerfUnit>();

    public P_Exercise(){}

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {

        this.schedule = schedule;
    }

    public ArrayList<PerfUnit> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<PerfUnit> records) {
        this.records = records;
    }


    public void addRecords(PerfUnit pf) {
        this.records = records;
        this.records.add(pf);
    }

    public P_Exercise(String schedule, ArrayList<PerfUnit> records) {
        this.schedule = schedule;
        this.records = records;
    }

    public P_Exercise(int id, String type, String exercise_name, String description, String imagePath, String author_name, String author_uid, boolean isPrivate, String schedule, ArrayList<PerfUnit> records) {
        super(id, type, exercise_name, description, imagePath, author_name, author_uid, isPrivate);
        this.schedule = schedule;
        this.records = records;
    }

    public P_Exercise(int id, String type, String exercise_name, String description, String imagePath, String author_name, String author_uid, boolean isPrivate, String schedule) {

        super(id, type, exercise_name, description, imagePath, author_name, author_uid, isPrivate);
        this.schedule = schedule;
    }

    public P_Exercise(int id, String type, String exercise_name, String description, String imagePath, String author_name, String author_uid, boolean isPrivate) {
        super(id, type, exercise_name, description, imagePath, author_name, author_uid, isPrivate);
    }

    public P_Exercise(int id, String exercise_name, String type) {
        super(id, exercise_name, type);
    }

    public String getFormattedSchedule(){

        ArrayList<Integer> arr = getWeekDaysArray();
        if (arr.get(0) == -1) return "Unlimited Schedule";
        return "Sun*" +arr.get(0)+ "|Mon*" +arr.get(1) +"|Tue*" +arr.get(2) + "|Wed*" +arr.get(3) + "|Thu*" + arr.get(4)+ "|Fri*" +arr.get(5) +"|Sat*" +arr.get(6);

    }
    public ArrayList<Integer> getWeekDaysArray(){

        ArrayList<Integer> arrToReturn= new ArrayList<>();

        for (int i=0;i<schedule.length();i++){
            if (Character.isDigit(schedule.charAt(i))){
                arrToReturn.add(Character.getNumericValue(schedule.charAt(i)));
            }
        }
        if (arrToReturn.size()==0) {
            for (int i=0;i<7;i++){
                arrToReturn.add(-1);
            }
        }

        return arrToReturn;

    }


    @Override
    public String toString() {
        return ""+getExercise_name() ;
    }
}
