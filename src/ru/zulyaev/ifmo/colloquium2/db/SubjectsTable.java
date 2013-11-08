package ru.zulyaev.ifmo.colloquium2.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * author: zulyaev
 */
public class SubjectsTable {
    public static final String TABLE_NAME = "subjects";
    public static final String COLUMN_TITLE = "title";
    public static final String[] ALL_COLUMNS = {
            COLUMN_TITLE
    };

    private static final String CREATE_TABLE_QUERY = String.format(
            "create table %s (" +
                    "_id integer primary key autoincrement," +
                    "%s text not null" +
            ")",
            TABLE_NAME,
            COLUMN_TITLE
    );

    private static final String SELECT_QUERY = String.format(
            "select %s._id, %s.%s, sum(%s.%s) as %s from %s left join %s on %s._id = %s.%s group by %s._id",
            TABLE_NAME,
            TABLE_NAME,
            COLUMN_TITLE,
            ScoresTable.TABLE_NAME,
            ScoresTable.COLUMN_SCORE,
            ScoresTable.COLUMN_SCORE,
            TABLE_NAME,
            ScoresTable.TABLE_NAME,
            TABLE_NAME,
            ScoresTable.TABLE_NAME,
            ScoresTable.COLUMN_SUBJECT,
            TABLE_NAME
    );
    private static final String GET_TITLE_QUERY = String.format(
            "select %s from %s where _id = ?",
            COLUMN_TITLE,
            TABLE_NAME
    );

    public static void init(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    private final SQLiteDatabase db;

    public SubjectsTable(SQLiteDatabase db) {
        this.db = db;
    }

    public Cursor selectAllWithScore() {
        return db.rawQuery(SELECT_QUERY, null);
    }

    public Cursor selectAll() {
        return db.rawQuery("select * from " + TABLE_NAME, null);
    }

    public String getSubjectTitle(long id) {
        Cursor cursor = db.rawQuery(GET_TITLE_QUERY, new String[] {Long.toString(id)});
        try {
            cursor.moveToNext();
            return cursor.getString(0);
        } finally {
            cursor.close();
        }
    }

    public void addSubject(String name) {
        ContentValues values = new ContentValues(1);
        values.put(COLUMN_TITLE, name);
        db.insert(TABLE_NAME, null, values);
    }

    public void modifySubject(long id, String name) {
        ContentValues values = new ContentValues(1);
        values.put(COLUMN_TITLE, name);
        db.update(TABLE_NAME, values, "_id = ?", new String[]{Long.toString(id)});
    }

    public void deleteSubject(long id) {
        db.delete(TABLE_NAME, "_id = ?", new String[]{Long.toString(id)});
    }
}
