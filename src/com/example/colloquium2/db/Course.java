package com.example.colloquium2.db;

/**
 * Created with IntelliJ IDEA.
 * User: gfv
 * Date: 08.11.13
 * Time: 13:39
 * To change this template use File | Settings | File Templates.
 */
public class Course {
    private long id;
    private String name;
    private long grade;

    public long getGrade() {
        return grade;
    }

    public void setGrade(long grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String toString() { return name + " (" + Long.toString(grade) + ")"; }
}
