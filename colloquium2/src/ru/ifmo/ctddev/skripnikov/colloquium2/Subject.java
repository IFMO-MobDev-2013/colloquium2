package ru.ifmo.ctddev.skripnikov.colloquium2;

import java.io.Serializable;

public class Subject implements Serializable {

    final long id;
    String name;
    long time;
    int rating;

    Subject(long id, String name, int rating, long time) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.time = time;
    }
}
