package com.example.HometaskScheduler;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Xottab
 * Date: 08.11.13
 * Time: 13:35
 * To change this template use File | Settings | File Templates.
 */
public class Subject implements Serializable {
    public Integer dbID;
    public String description;
    public Integer currentPoints;

    public Subject(int dbID, String description, int currentPoints) {
        this.dbID = dbID;
        this.description = description;
        this.currentPoints = currentPoints;
    }

    public Subject(String description) {
        this.description = description;
        this.currentPoints = 0;
    }
}
