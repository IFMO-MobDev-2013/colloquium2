package com.example.HometaskScheduler;

/**
 * Created with IntelliJ IDEA.
 * User: Xottab
 * Date: 08.11.13
 * Time: 13:11
 * To change this template use File | Settings | File Templates.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ScheduleDAO {


    private static final String SUBJECT_ID = "subjectID";
    private static final String DESCRIPTION = "description";
    private static final String MARK_ID = "markID";
    private static final String MARK_POINTS = "points";
    private static final String CURRENT_MARK = "current_mark";

    private DatabaseHelper helper;
    private SQLiteDatabase db;

    private static final String SUBJECTS_TABLE_CREATE =
            "create table if not exists " +
                    "subjects (subjectID integer primary key autoincrement, " +
                    "description text not null, " +
                    "current_mark integer not null default (0));";
    private static final String MARKS_TABLE_CREATE = "create table if not exists marks " +
            "(markID integer primary key autoincrement, " +
            "subjectID integer not null, " +
            "description text not null, " +
            "points integer not null default (0));";

    private static final String DATABASE_NAME = "mydb";
    private static final String TABLE_SUBJECT = "subjects";
    private static final String TABLE_MARK = "marks";
    private static final int DATABASE_VERSION = 2;

    private final Context context;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SUBJECTS_TABLE_CREATE);
            db.execSQL(MARKS_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //erase tables
            db.execSQL("DROP TABLE IF EXISTS subjects");
            db.execSQL("DROP TABLE IF EXISTS marks");
            onCreate(db);
        }
    }


    public ScheduleDAO(Context context) {
        this.context = context;
    }


    private ScheduleDAO open() throws SQLException {
        helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
        return this;
    }

    private void close() {
        helper.close();
    }

    private Cursor fetchAllSubjects() {
        return db.query(TABLE_SUBJECT, new String[]{SUBJECT_ID, DESCRIPTION,
                CURRENT_MARK}, null, null, null, null, null);
    }

    private Cursor fetchAllMarks(Integer subjectID) {
        return db.query(TABLE_MARK, new String[]{MARK_ID, DESCRIPTION, SUBJECT_ID,
                MARK_POINTS}, SUBJECT_ID + " = " + subjectID, null, null, null, null);
    }

    public ArrayList<Subject> getAllSubjects() throws SQLException {
        this.open();
        Cursor cursor = fetchAllSubjects();
        ArrayList<Subject> result = new ArrayList<>();
        int i1 = cursor.getColumnIndex(SUBJECT_ID);
        int i2 = cursor.getColumnIndex(DESCRIPTION);
        int i3 = cursor.getColumnIndex(CURRENT_MARK);
        while (cursor.moveToNext()) {
            Subject subject = new Subject(cursor.getInt(i1), cursor.getString(i2), cursor.getInt(i3));
            ArrayList<Mark> marks = getAllMarks(subject.dbID);
            int t=0;
            for(Mark mark:marks){
                t+=mark.points;
            }
            subject.currentPoints = t;
            result.add(subject);
        }
        this.close();
        return result;
    }

    public ArrayList<Mark> getAllMarks(Integer subjectID) throws SQLException {
        this.open();
        Cursor cursor = fetchAllMarks(subjectID);
        ArrayList<Mark> result = new ArrayList<>();
        int i1 = cursor.getColumnIndex(MARK_ID);
        int i2 = cursor.getColumnIndex(SUBJECT_ID);
        int i3 = cursor.getColumnIndex(DESCRIPTION);
        int i4 = cursor.getColumnIndex(MARK_POINTS);
        while (cursor.moveToNext()) {
            result.add(new Mark(cursor.getInt(i1), cursor.getInt(i2), cursor.getString(i3), cursor.getInt(i4)));
        }
        this.close();
        return result;
    }


    public void addSubject(Subject subject) throws SQLException {
        try {
            this.open();
            ContentValues initialValues = new ContentValues();
            initialValues.put(DESCRIPTION, subject.description);
            long result = db.insert(TABLE_SUBJECT, null, initialValues);
            subject.dbID = (int) result;
            this.close();
        } finally {
            this.close();
        }

    }

    public void addMark(Mark mark) throws SQLException {
        try {
            this.open();
            ContentValues initialValues = new ContentValues();
            initialValues.put(DESCRIPTION, mark.description);
            initialValues.put(SUBJECT_ID, mark.subjectID);
            initialValues.put(MARK_POINTS, mark.points);
            long result = db.insert(TABLE_MARK, null, initialValues);
            mark.dbID = (int) result;
            this.close();
        } finally {
            this.close();
        }

    }

    public void editMark(Mark mark) {
        try {
            this.open();
            ContentValues initialValues = new ContentValues();
            initialValues.put(DESCRIPTION, mark.description);
            initialValues.put(MARK_POINTS, mark.points);
            initialValues.put(SUBJECT_ID, mark.subjectID);
            long result = db.update(TABLE_MARK, initialValues, MARK_ID + " = " + mark.dbID, null);
            this.close();
        } finally {
            this.close();
        }
    }

    public void editSubject(Subject subject) {
        try {
            this.open();
            ContentValues initialValues = new ContentValues();
            initialValues.put(DESCRIPTION, subject.description);
            long result = db.update(TABLE_SUBJECT, initialValues, SUBJECT_ID + " = " + subject.dbID, null);
            this.close();
        } finally {
            this.close();
        }
    }

    public void deleteMark(Integer markID) {
        try {
            this.open();
            long result = db.delete(TABLE_MARK, MARK_ID + " = " + markID, null);
            this.close();
        } finally {
            this.close();
        }
    }

    public void deleteSubject(Integer subjectID) {
        try {
            this.open();
            long result = db.delete(TABLE_SUBJECT, SUBJECT_ID + " = " + subjectID, null);
            this.close();
        } finally {
            this.close();
        }
    }
}

