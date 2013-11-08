package com.example.HometaskScheduler;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Xottab
 * Date: 08.11.13
 * Time: 13:35
 * To change this template use File | Settings | File Templates.
 */
public class Mark implements Serializable {
    public int dbID;
    public int subjectID;
    public String description;
    public int points;

    public Mark(int dbID, int subjectID, String descriptiion, int points) {
        this.dbID = dbID;
        this.subjectID = subjectID;
        this.description = descriptiion;
        this.points = points;
    }

    public Mark(int subjectID, String description, int points) {
        this.subjectID = subjectID;
        this.description = description;
        this.points = points;
    }
}
