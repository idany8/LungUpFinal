package com.example.idan.lungupfinal.soundmeter;


 public class ScoreUnit {
    public int score = 0;
    public String name = "";

    public ScoreUnit(){

    }
    public ScoreUnit(int score, String name) {
        this.score=score;
        this.name=name;
    }

               public int getScore(){
                   return score;
               }
               public String getName(){
                   return name;
               }
}
