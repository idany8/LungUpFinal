package com.example.idan.lungupfinal;

import java.util.ArrayList;

/**
 * Created by Idan on 17/04/2017.
 */

public class P_Exercise extends Exercise {

    private String schedule;
    private ArrayList<PerfUnit> records;

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

}
