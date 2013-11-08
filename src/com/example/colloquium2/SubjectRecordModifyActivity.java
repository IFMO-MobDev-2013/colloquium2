package com.example.colloquium2;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.example.colloquium2.databases.SubjectRecordsDatabase;

/**
 * Created with IntelliJ IDEA.
 * User: Genyaz
 * Date: 08.11.13
 * Time: 13:11
 * To change this template use File | Settings | File Templates.
 */
public class SubjectRecordModifyActivity extends Activity {

    private EditText subjectRecordName, subjectRecordScore;
    private int recordId;
    private int subjectId;
    private SQLiteDatabase rdb, wdb;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_record_modify);
        subjectRecordName = (EditText) findViewById(R.id.subject_record_name_edit);
        subjectRecordScore = (EditText) findViewById(R.id.subject_score_edit);
        recordId = getIntent().getIntExtra(getString(R.string.record_id), -1);
        subjectId= getIntent().getIntExtra(getString(R.string.subject_id), -1);
        SubjectRecordsDatabase srdb = new SubjectRecordsDatabase(this);
        rdb = srdb.getReadableDatabase();
        Cursor cursor = rdb.query(SubjectRecordsDatabase.DATABASE_NAME, null,
                SubjectRecordsDatabase._ID + " = " + recordId, null, null, null, null, "1");
        while (cursor.moveToNext()) {
            subjectRecordName.setText(cursor.getString(cursor.getColumnIndex(SubjectRecordsDatabase.RECORD_NAME)));
            subjectRecordScore.setText(cursor.getInt(cursor.getColumnIndex(SubjectRecordsDatabase.RECORD_POINTS)) + "");
        }
        cursor.close();
        rdb.close();
        srdb.close();
    }

    public void onSubjectRecordProceed(View view) {
        if (subjectRecordName.getText().toString().isEmpty()) {
            onSubjectRecordRemove(view);
        } else {
            if (recordId == -1) {
                SubjectRecordsDatabase srdb = new SubjectRecordsDatabase(this);
                wdb = srdb.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(SubjectRecordsDatabase.RECORD_NAME, subjectRecordName.getText().toString());
                cv.put(SubjectRecordsDatabase.RECORD_POINTS, subjectRecordScore.getText().toString());
                cv.put(SubjectRecordsDatabase.SUBJECT_ID, subjectId + "");
                wdb.insert(SubjectRecordsDatabase.DATABASE_NAME, null, cv);
                wdb.close();
                srdb.close();
            } else {
                SubjectRecordsDatabase srdb = new SubjectRecordsDatabase(this);
                wdb = srdb.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(SubjectRecordsDatabase.RECORD_NAME, subjectRecordName.getText().toString());
                cv.put(SubjectRecordsDatabase.RECORD_POINTS, subjectRecordScore.getText().toString());
                cv.put(SubjectRecordsDatabase.SUBJECT_ID, subjectId + "");
                wdb.update(SubjectRecordsDatabase.DATABASE_NAME, cv, SubjectRecordsDatabase._ID + " = " + recordId, null);
                wdb.close();
                srdb.close();
            }
        }
        this.finish();
    }

    public void onSubjectRecordRemove(View view) {
        if (recordId != -1) {
            SubjectRecordsDatabase srdb = new SubjectRecordsDatabase(this);
            wdb = srdb.getWritableDatabase();
            wdb.delete(SubjectRecordsDatabase.DATABASE_NAME, SubjectRecordsDatabase._ID + " = " + recordId, null);
            wdb.close();
            srdb.close();
        }
        this.finish();
    }
}