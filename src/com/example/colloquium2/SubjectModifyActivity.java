package com.example.colloquium2;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.example.colloquium2.databases.SubjectRecordsDatabase;
import com.example.colloquium2.databases.SubjectsDatabase;

/**
 * Created with IntelliJ IDEA.
 * User: Genyaz
 * Date: 08.11.13
 * Time: 13:11
 * To change this template use File | Settings | File Templates.
 */
public class SubjectModifyActivity extends Activity {

    private EditText subjectName;
    private int subjectId;
    private SQLiteDatabase rdb;
    private SQLiteDatabase wdb;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_modify);
        subjectName = (EditText) findViewById(R.id.subject_name);
        subjectId = getIntent().getIntExtra(getString(R.string.subject_id), -1);
        if (subjectId != -1) {
            String name = "";
            SubjectsDatabase db = new SubjectsDatabase(this);
            rdb = db.getReadableDatabase();
            Cursor cursor = rdb.query(SubjectsDatabase.DATABASE_NAME, null,
                    SubjectsDatabase._ID + " = " + subjectId, null, null, null, null, "1");
            while (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex(SubjectsDatabase.SUBJECT_NAME));
            }
            cursor.close();
            rdb.close();
            db.close();
            subjectName.setText(name);
        } else {
            subjectName.setText("");
        }
    }

    public void onProceed(View view) {
        if (subjectName.getText().toString().isEmpty()) {
            onRemove(view);
        } else {
            if (subjectId == -1) {
                SubjectsDatabase db = new SubjectsDatabase(this);
                wdb = db.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(SubjectsDatabase.SUBJECT_NAME, subjectName.getText().toString());
                wdb.insert(SubjectsDatabase.DATABASE_NAME, null, cv);
                wdb.close();
                db.close();
            } else {
                SubjectsDatabase db = new SubjectsDatabase(this);
                wdb = db.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(SubjectsDatabase.SUBJECT_NAME, subjectName.getText().toString());
                wdb.update(SubjectsDatabase.DATABASE_NAME, cv, SubjectsDatabase._ID + " = " + subjectId, null);
                wdb.close();
                db.close();
            }
        }
        this.finish();
    }

    public void onRemove(View view) {
        if (subjectId != -1) {
            SubjectsDatabase db = new SubjectsDatabase(this);
            wdb = db.getWritableDatabase();
            wdb.delete(SubjectsDatabase.DATABASE_NAME, SubjectsDatabase._ID + " = " + subjectId, null);
            wdb.close();
            db.close();
            SubjectRecordsDatabase srdb = new SubjectRecordsDatabase(this);
            wdb = srdb.getWritableDatabase();
            wdb.delete(SubjectRecordsDatabase.DATABASE_NAME, SubjectRecordsDatabase.SUBJECT_ID + " = " + subjectId, null);
            wdb.close();
            srdb.close();
        }
        this.finish();
    }
}