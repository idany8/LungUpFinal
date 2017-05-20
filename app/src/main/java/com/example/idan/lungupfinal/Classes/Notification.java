package com.example.idan.lungupfinal.Classes;

/**
 * Created by Idan on 20/05/2017.
 */

public class Notification  {
    private String title;
    private String body;
    private String recieverToken;


    public Notification(String title, String body, String recieverToken) {
        this.title = title;
        this.body = body;
        this.recieverToken = recieverToken;
    }

    public Notification() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getRecieverToken() {
        return recieverToken;
    }

    public void setRecieverToken(String recieverToken) {
        this.recieverToken = recieverToken;
    }
}



