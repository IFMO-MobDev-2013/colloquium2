package ru.ifmo.ctddev.skripnikov.colloquium2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ru.ifmo.ctddev.skripnikov.colloquium2.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_SUBJECTS = "items";
    public static final String TABLE_RATING = "ratings";

    public static final String DROP_TABLE_SUBJECTS = "drop table if exists " + TABLE_SUBJECTS;
    public static final String DROP_TABLE_RATING = "drop table if exists " + TABLE_RATING;

    private static final String CREATE_SUBJECTS_QUERY = "create table "
            + TABLE_SUBJECTS + "("
            + SubjectCols.ID + " integer not null primary key autoincrement, "
            + SubjectCols.NAME + " text unique on conflict ignore, "
            + SubjectCols.RATING + " integer not null, "
            + SubjectCols.TIME + " integer not null)";

    private static final String CREATE_RATING_QUERY = "create table "
            + TABLE_RATING + "("
            + RatingCols.ID + " integer not null primary key autoincrement, "
            + RatingCols.SUBJECT_ID + " integer not null, "
            + RatingCols.VALUE + " integer not null, "
            + RatingCols.DESCRIPTION + " text, "
            + RatingCols.TIME + " integer not null, "
            + "foreign key (" + RatingCols.SUBJECT_ID + ") references "
            + TABLE_SUBJECTS + "(" + SubjectCols.ID + ") on delete cascade)";

    public static final class SubjectCols {
        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String RATING = "rating";
        public static final String TIME = "time";
    }

    public static final class RatingCols {
        public static final String ID = "_id";
        public static final String SUBJECT_ID = "subjectid";
        public static final String VALUE = "value";
        public static final String DESCRIPTION = "description";
        public static final String TIME = "time";
    }

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SUBJECTS_QUERY);
        db.execSQL(CREATE_RATING_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_SUBJECTS);
        db.execSQL(DROP_TABLE_RATING);
        onCreate(db);
    }
}