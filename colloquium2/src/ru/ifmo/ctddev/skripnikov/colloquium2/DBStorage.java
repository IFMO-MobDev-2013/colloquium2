package ru.ifmo.ctddev.skripnikov.colloquium2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ru.ifmo.ctddev.skripnikov.colloquium2.SQLiteHelper.*;

import java.util.ArrayList;

public class DBStorage {
    private SQLiteDatabase database;

    public DBStorage(Context context) {
        SQLiteHelper helper = new SQLiteHelper(context);
        database = helper.getWritableDatabase();
        database.execSQL("PRAGMA foreign_keys=ON;");
    }

    public void destroy() {
        database.close();
    }

    public Subject[] getSubjects() {
        Cursor cursor = null;
        try {
            cursor = database.query(SQLiteHelper.TABLE_SUBJECTS,
                    new String[]{SubjectCols.ID, SubjectCols.NAME, SubjectCols.RATING, SubjectCols.TIME},
                    null, null, null, null, null);
            ArrayList<Subject> answer = new ArrayList<Subject>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                answer.add(new Subject(cursor.getLong(0), cursor.getString(1), cursor.getInt(2), cursor.getLong(3)));
                cursor.moveToNext();
            }
            return answer.toArray(new Subject[answer.size()]);
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public Rating[] getRatingsBySubjectId(Long subjectId) {
        String where = RatingCols.SUBJECT_ID + "=" + subjectId;
        Cursor cursor = null;
        try {
            cursor = database.query(SQLiteHelper.TABLE_RATING,
                    new String[]{RatingCols.ID, RatingCols.VALUE, RatingCols.DESCRIPTION, RatingCols.TIME},
                    where, null, null, null, null);
            ArrayList<Rating> answer = new ArrayList<Rating>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                answer.add(new Rating(cursor.getLong(0), cursor.getInt(1), cursor.getString(2), cursor.getLong(3)));
                cursor.moveToNext();
            }

            return answer.toArray(new Rating[answer.size()]);
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public long addSubject(String name) {
        name = name.trim();
        ContentValues values = new ContentValues();
        values.put(SubjectCols.NAME, name);
        values.put(SubjectCols.RATING, 0);
        values.put(SubjectCols.TIME, System.currentTimeMillis());
        return database.insert(SQLiteHelper.TABLE_SUBJECTS, null, values);
    }

    public boolean changeSubject(Subject subject) {
        String where = SubjectCols.ID + " = " + subject.id;
        ContentValues values = new ContentValues();
        values.put(SubjectCols.NAME, subject.name);
        values.put(SubjectCols.RATING, subject.rating);
        values.put(SubjectCols.TIME, subject.time);
        return database.update(SQLiteHelper.TABLE_SUBJECTS, values, where, null) > 0;
    }

    public boolean deleteSubject(long id) {
        String where = SubjectCols.ID + " = " + id;
        return database.delete(SQLiteHelper.TABLE_SUBJECTS, where, null) > 0;
    }

    public long addRating(long subjectId, int value, String description) {

        ContentValues values = new ContentValues();
        values.put(RatingCols.SUBJECT_ID, subjectId);
        values.put(RatingCols.VALUE, value);
        values.put(RatingCols.DESCRIPTION, description);
        values.put(RatingCols.TIME, System.currentTimeMillis());
        return database.insert(SQLiteHelper.TABLE_RATING, null, values);
    }

    public boolean changeRating(Rating rating) {
        String where = RatingCols.ID + " = " + rating.id;
        ContentValues values = new ContentValues();
        values.put(RatingCols.VALUE, rating.value);
        values.put(RatingCols.DESCRIPTION, rating.description);
        values.put(RatingCols.TIME, rating.time);
        return database.update(SQLiteHelper.TABLE_RATING, values, where, null) > 0;
    }

    public boolean deleteRating(long id) {
        String where = RatingCols.ID + " = " + id;
        return database.delete(SQLiteHelper.TABLE_RATING, where, null) > 0;
    }
}
