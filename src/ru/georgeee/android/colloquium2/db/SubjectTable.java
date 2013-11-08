package ru.georgeee.android.colloquium2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import ru.georgeee.android.colloquium2.model.Subject;

import java.io.IOException;
import java.util.Date;

public class SubjectTable extends Table<ru.georgeee.android.colloquium2.model.Subject> {
    private static SubjectTable instance;

    public static SubjectTable getInstance(Context context){
        if(instance == null) instance = new SubjectTable(context);
        return instance;
    }

    private SubjectTable(Context context) {
        super(context);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    protected static final String DB_TABLE = "subject";
    protected static final String COLUMN_SUBJECT_ID = "subject_id";
    protected static final String COLUMN_NAME = "name";
    protected static final String DB_CREATE =
            "create table " + DB_TABLE + "(" +
                    COLUMN_SUBJECT_ID + " integer primary key autoincrement, " +
                    COLUMN_NAME + " text" +
                    ");";

    @Override
    protected Subject readModelInstance(Cursor cursor) throws IOException, ClassNotFoundException {
        Subject subject = new Subject();

        subject.setSubjectId(cursor.getLong(cursor.getColumnIndex(COLUMN_SUBJECT_ID)));
        subject.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));



        return subject;
    }


    @Override
    protected Class<Subject> getModelClass() {
        return Subject.class;
    }
    public void save(Subject subject) throws IOException {
        saveFeed(subject, false);
    }
    public void saveFeed(Subject subject, boolean createOnly) throws IOException {
        ContentValues cv = new ContentValues();
        long subjectId = subject.getSubjectId();

        cv.put(COLUMN_NAME, subject.getName());

        if (subjectId != 0) {
            if(!createOnly) mDB.update(DB_TABLE, cv, COLUMN_SUBJECT_ID + " = " + subjectId, null);
        } else {
            subjectId = mDB.insert(DB_TABLE, null, cv);
        }

        subject.setSubjectId(subjectId);
    }

    public void delete(Subject subject) {
        mDB.delete(DB_TABLE, COLUMN_SUBJECT_ID + " = " + subject.getSubjectId(), null);
    }

    public Subject[] loadSubjects(int offset, int limit) throws IOException, ClassNotFoundException {
        return readAllFromCursor(mDB.query(DB_TABLE, null, null, null, null, null, COLUMN_SUBJECT_ID + " DESC", offset + ", " + limit));
    }

    public Subject[] loadSubjects() throws IOException, ClassNotFoundException {
        return readAllFromCursor(mDB.query(DB_TABLE, null, null, null, null, null, COLUMN_SUBJECT_ID + " DESC"));
    }

    public Subject loadSubject(long feedId) throws IOException, ClassNotFoundException {
        Subject[] subjects = readAllFromCursor(mDB.query(DB_TABLE, null, COLUMN_SUBJECT_ID + "=" + feedId, null, null, null, null));
        return subjects.length == 0 ? null : subjects[0];
    }
}