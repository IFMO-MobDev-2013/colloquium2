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
    private String[] allColumns = { CourseSQLHelper.COLUMN_ID, CourseSQLHelper.COLUMN_NAME };

    private final String COURSE_ALL_QUERY = "select courses._id as _id, courses.name as name, sum(grades.grade) as grade from courses left outer join grades on grades.course_id = courses._id group by courses._id;";

    public GradesDataSource(Context context) {
        dbHelper = new CourseSQLHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Course addGrade(String name, int grade) {
        ContentValues values = new ContentValues();
        values.put(CourseSQLHelper.COLUMN_NAME, name);
        long insertId = database.insert(CourseSQLHelper.TABLE_COURSES, null, values);
        Log.d("COL2-Q", Long.toString(insertId));

        Cursor cursor = database.rawQuery(COURSE_QUERY_BY_ID, new String[]{Long.toString(insertId)});
        cursor.moveToFirst();
        Course newCourse = cursorToCourse(cursor);
        cursor.close();
        return newCourse;
    }

    public void deleteCourse(long id) {
        database.delete(CourseSQLHelper.TABLE_COURSES, "_id = ?", new String[]{Long.toString(id)});
    }

    public Cursor getAllCourses() {
        Cursor cursor = database.rawQuery(COURSE_ALL_QUERY, null);
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
