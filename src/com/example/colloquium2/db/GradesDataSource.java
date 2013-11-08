package com.example.colloquium2.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class GradesDataSource {

    // Database fields
    private SQLiteDatabase database;
    private CourseSQLHelper dbHelper;

    public GradesDataSource(Context context) {
        dbHelper = new CourseSQLHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Course addGrade(String name, int course_id, int grade) {
        ContentValues values = new ContentValues();
        values.put(CourseSQLHelper.COLUMN_NAME, name);
        values.put(CourseSQLHelper.COLUMN_COURSE_ID, course_id);
        values.put(CourseSQLHelper.COLUMN_GRADE, grade);
        long insertId = database.insert(CourseSQLHelper.TABLE_GRADES, null, values);

        Cursor cursor = database.rawQuery("select * from grades where _id = ?", new String[]{Long.toString(insertId)});
        cursor.moveToFirst();
        Course newCourse = cursorToCourse(cursor);
        cursor.close();
        return newCourse;
    }

    public void deleteCourse(long id) {
        database.delete(CourseSQLHelper.TABLE_GRADES, "_id = ?", new String[]{Long.toString(id)});
    }

    public Cursor getGrades(long course_id) {
        Cursor cursor = database.rawQuery("select * from grades where course_id = ?", new String[]{Long.toString(course_id)});
        return cursor;
    }

    private Course cursorToCourse(Cursor cursor) {
        Course course = new Course();
        course.setId(cursor.getLong(0));
        course.setName(cursor.getString(1));
        course.setGrade(cursor.getLong(2));
        return course;
    }
}
