package com.example.idan.lungupfinal.Classes;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Idan on 08/05/2017.
 */

public class PerfUnit {

    private long time;
    private double Score;
    private String ex_name;

    public PerfUnit(long time, double score) {
        this.time = time;
        Score = score;
    }

    public PerfUnit(long time, double score, String ex_name) {
        this.time = time;
        Score = score;
        this.ex_name = ex_name;
    }

    @Override
    public String toString() {

        String sdf = new SimpleDateFormat ("dd/MM/yyyy HH:mm:ss").format(time) ;
        return sdf +" | " + ex_name;

    }

    public PerfUnit() {
    }

    public PerfUnit(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getScore() {
        return Score;
    }

    public void setScore(double score) {
        Score = score;
    }

    public String getEx_name() {
        return ex_name;
    }

    public void setEx_name(String ex_name) {
        this.ex_name = ex_name;
    }
}

