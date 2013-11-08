package ru.zulyaev.ifmo.colloquium2.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * author: zulyaev
 */
public class ScoresTable {
    public static final String TABLE_NAME = "scores";

    public static final String COLUMN_SUBJECT = "subject_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_SCORE = "score";

    public static final String[] ALL_COLUMNS = {
            "_id",
            COLUMN_SUBJECT,
            COLUMN_TITLE,
            COLUMN_SCORE
    };

    private static final String CREATE_TABLE_QUERY = String.format(
            "create table %s (" +
                    "_id integer primary key autoincrement," +
                    "%s integer not null," +
                    "%s text not null," +
                    "%s integer not null" +
            ")",
            TABLE_NAME,
            COLUMN_SUBJECT,
            COLUMN_TITLE,
            COLUMN_SCORE
    );

    private static final String SELECT_SUM_QUERY = String.format(
            "select sum(%s) from %s where %s = ?",
            COLUMN_SCORE,
            TABLE_NAME,
            COLUMN_SUBJECT
    );

    public static void init(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    private final SQLiteDatabase db;

    public ScoresTable(SQLiteDatabase db) {
        this.db = db;
    }

    public Cursor selectAllById(long id) {
        return db.query(TABLE_NAME, ALL_COLUMNS, COLUMN_SUBJECT + " = ?", new String[]{Long.toString(id)}, null, null, null);
    }

    public int getSumScore(long id) {
        Cursor cursor = db.rawQuery(SELECT_SUM_QUERY, new String[]{Long.toString(id)});
        try {
            cursor.moveToNext();
            return cursor.getInt(0);
        } finally {
            cursor.close();
        }
    }

    public void addScore(long subjectId, String title, int score) {
        ContentValues values = new ContentValues(3);
        values.put(COLUMN_SUBJECT, subjectId);
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_SCORE, score);
        db.insert(TABLE_NAME, null, values);
    }

    public void modifyScore(long id, String title, int score) {
        ContentValues values = new ContentValues(2);
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_SCORE, score);
        db.update(TABLE_NAME, values, "_id = ?", new String[]{Long.toString(id)});
    }

    public void deleteScore(long id) {
        db.delete(TABLE_NAME, "_id = ?", new String[]{Long.toString(id)});
    }
}
