package com.example.idan.lungupfinal;

import java.util.Date;

/**
 * Created by Idan on 08/05/2017.
 */

public class PerfUnit {

    private long time;
    private double Score;

    public PerfUnit(long time, double score) {
        this.time = time;
        Score = score;
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
}

