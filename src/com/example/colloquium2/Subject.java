package com.example.colloquium2;


public class Subject {
    public int id_subject = 0;
    public String name = "";
    public int points = 0;

    public Subject(String name)
    {
        this.name = name;
    }

    public Subject(int id_subject)
    {
        this.id_subject = id_subject;
    }
}
