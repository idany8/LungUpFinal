package com.example.idan.lungupfinal.Classes;

import java.util.ArrayList;

/**
 * Created by Idan on 16/04/2017.
 */

public class Patient extends User {
    ArrayList<P_Exercise> p_exercises = new ArrayList<P_Exercise>();

    public Patient() {
    }

    public Patient(ArrayList<P_Exercise> p_exercises) {
        this.p_exercises = p_exercises;
    }

    public Patient(String email, String name, String type, String uid, boolean initialized, ArrayList<String> related_users_uid, ArrayList<P_Exercise> p_exercises) {
        super(email, name, type, uid, initialized, related_users_uid);
        this.p_exercises = p_exercises;
    }

    public ArrayList<P_Exercise> getP_exercises() {

        return p_exercises;
    }

    public void setP_exercises(ArrayList<P_Exercise> p_exercises) {
        this.p_exercises = p_exercises;
    }
}
