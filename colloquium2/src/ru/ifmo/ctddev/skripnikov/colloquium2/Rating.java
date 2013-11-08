package ru.ifmo.ctddev.skripnikov.colloquium2;

public class Rating {

    final long id;
    int value;
    String description;
    long time;

    Rating(long id, int value, String description, long time) {
        this.id = id;
        this.value = value;
        this.description = description;
        this.time = time;
    }
}
