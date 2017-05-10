package com.example.idan.lungupfinal;

import java.util.Date;

/**
 * Created by Idan on 08/05/2017.
 */

public class PerfUnit {

    private Date time;
    private double Score;

    public PerfUnit(Date time, double score) {
        this.time = time;
        Score = score;
    }

    public PerfUnit() {
    }

    public PerfUnit(Date time) {
        this.time = time;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public double getScore() {
        return Score;
    }

    public void setScore(double score) {
        Score = score;
    }
}

