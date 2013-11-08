package com.example.PashaAC.Diary;

import android.util.Pair;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: ПАВЕЛ
 * Date: 08.11.13
 * Time: 13:00
 * To change this template use File | Settings | File Templates.
 */
public class Subject {
    private String subject;
    private int score;
    private ArrayList<String> partOfSubject;
    private ArrayList<Integer> partOfScore;

    public Subject(String subject) {
        this.subject = subject;
        this.score = 0;
        partOfSubject = new ArrayList<String>();
        partOfScore = new ArrayList<Integer>();
    }

    public void addItem(String name, int score) {
        partOfSubject.add(name);
        partOfScore.add(score);
        this.score += score;
    }

    public int getScore() {
        return score;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return subject;
    }
}
