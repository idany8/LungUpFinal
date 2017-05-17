package com.example.idan.lungupfinal;

import java.io.Serializable;

/**
 * Created by Idan on 17/04/2017.
 */

public class Exercise implements Serializable {
    private int id;
    private String type;
    private String exercise_name;
    private String description;
    private String imagePath;
    private String author_name;
    private String author_uid;
    private boolean isPrivate;

    public Exercise(){

    }
    public Exercise(int id, String type, String exercise_name, String description, String imagePath, String author_name, String author_uid, boolean isPrivate) {
        this.id = id;
        this.type = type;
        this.exercise_name = exercise_name;
        this.description = description;
        this.imagePath = imagePath;
        this.author_name = author_name;
        this.author_uid = author_uid;
        this.isPrivate = isPrivate;
    }

    public Exercise(int id, String exercise_name, String type) {
        this.id = id;
        this.exercise_name = exercise_name;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExercise_name() {
        return exercise_name;
    }

    public void setExercise_name(String exercise_name) {
        this.exercise_name = exercise_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getAuthor_uid() {
        return author_uid;
    }

    public void setAuthor_uid(String author_uid) {
        this.author_uid = author_uid;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }
}
