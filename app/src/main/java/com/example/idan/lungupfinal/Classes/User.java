package com.example.idan.lungupfinal.Classes;

import java.util.ArrayList;

public class User {

    private String email;
    private String name;
    private String type;
    private String uid;
    ArrayList<String> related_users_uid = new ArrayList<String>();
    boolean initialized ;

    public User() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public User(String email, String name, String type, String uid, boolean initialized, ArrayList<String> related_users_uid) {
        this.email = email;
        this.name = name;
        this.type = type;
        this.uid =uid;

        this.initialized = initialized;
        this.related_users_uid = related_users_uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getRelatedUsers() {
        return related_users_uid;
    }

    public void setRelatedUsers(ArrayList<String> related_users_uid) {
        this.related_users_uid = related_users_uid;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }




}
