package ru.georgeee.android.colloquium2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import ru.georgeee.android.colloquium2.model.Mark;
import ru.georgeee.android.colloquium2.model.Subject;

import java.io.IOException;

public class MarkTable extends Table<Mark> {

    protected static final String DB_TABLE = "mark";
    protected static final String COLUMN_MARK_ID = "mark_id";
    protected static final String COLUMN_SUBJECT_ID = "subject_id";
    protected static final String COLUMN_NAME = "name";
    protected static final String COLUMN_VALUE = "value";
    protected static final String DB_CREATE =
            "create table " + DB_TABLE + "(" +
                    COLUMN_MARK_ID + " integer primary key autoincrement, " +
                    COLUMN_SUBJECT_ID + " integer, " +
                    COLUMN_NAME + " text, " +
                    COLUMN_VALUE + " integer" +
                    ");";
    private static MarkTable instance;

    private MarkTable(Context context) {
        super(context);
    }

    public static MarkTable getInstance(){
        return instance;
    }

    public static MarkTable getInstance(Context context) {
        if (instance == null) instance = new MarkTable(context);
        return instance;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    protected Class<Mark> getModelClass() {
        return Mark.class;
    }

    @Override
    protected Mark readModelInstance(Cursor cursor) throws IOException, ClassNotFoundException {
        Mark mark = new Mark();
        mark.setMarkId(cursor.getLong(cursor.getColumnIndex(COLUMN_MARK_ID)));
        mark.setSubjectId(cursor.getLong(cursor.getColumnIndex(COLUMN_SUBJECT_ID)));
        mark.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
        mark.setValue(cursor.getInt(cursor.getColumnIndex(COLUMN_VALUE)));
        return mark;
    }

    public void save(Mark mark) throws IOException {
        save(mark, false);
    }

    public void save(Mark mark, boolean createOnly) throws IOException {
        ContentValues cv = new ContentValues();
        long markId = mark.getMarkId();

        cv.put(COLUMN_SUBJECT_ID, mark.getSubjectId());
        cv.put(COLUMN_NAME, mark.getName());
        cv.put(COLUMN_VALUE, mark.getValue());

        if (markId != 0) {
            if (!createOnly) mDB.update(DB_TABLE, cv, COLUMN_MARK_ID + " = " + markId, null);
        } else {
            markId = mDB.insert(DB_TABLE, null, cv);
        }
        mark.setMarkId(markId);
    }

    public void deleteAllFromSubject(Subject subject) {
        deleteAllFromSubject(subject.getSubjectId());
    }

    public void deleteAllFromSubject(long subjectId) {
        mDB.delete(DB_TABLE, COLUMN_SUBJECT_ID + " = " + subjectId, null);
    }

    public void delete(long markId) {
        mDB.delete(DB_TABLE, COLUMN_MARK_ID + " = " + markId, null);
    }

    public void delete(Mark mark) {
        this.deleteAllFromSubject(mark.getMarkId());
    }

    public Mark[] loadMarks(long feedId, int offset, int limit) throws IOException, ClassNotFoundException {
        return readAllFromCursor(mDB.query(DB_TABLE, null, COLUMN_SUBJECT_ID + " = " + feedId, null, null, null, COLUMN_MARK_ID + " DESC", offset + ", " + limit));
    }

    public Mark[] loadMarks(long feedId) throws IOException, ClassNotFoundException {
        return readAllFromCursor(mDB.query(DB_TABLE, null, COLUMN_SUBJECT_ID + " = " + feedId, null, null, null, COLUMN_MARK_ID + " DESC"));
    }

    public Mark[] loadMarks(Subject subject, int offset, int limit) throws IOException, ClassNotFoundException {
        return loadMarks(subject.getSubjectId(), offset, limit);
    }

    public Mark[] loadMarks(Subject subject, int limit) throws IOException, ClassNotFoundException {
        return loadMarks(subject.getSubjectId(), 0, limit);
    }

    public Mark[] loadMarks(int feedId, int limit) throws IOException, ClassNotFoundException {
        return loadMarks(feedId, 0, limit);
    }

    public Mark[] loadMarks(Subject subject) throws IOException, ClassNotFoundException {
        return loadMarks(subject.getSubjectId());
    }


    public int getSumBySubjectId(long subjectId){
        Cursor cursor = mDB.query(DB_TABLE, new String[]{"SUM("+COLUMN_VALUE+")"}, COLUMN_SUBJECT_ID+" = "+subjectId, null, null, null, null, null);
        cursor.moveToNext();
        int result = cursor.getInt(0);
        cursor.close();
        return result;
    }
}