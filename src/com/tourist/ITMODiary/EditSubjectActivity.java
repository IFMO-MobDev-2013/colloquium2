package com.tourist.ITMODiary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditSubjectActivity extends Activity {

    public static final String EX_SUBJECT = "name";
    public static final String EX_ID = "id";

    private DBAdapter myDBAdapter;

    private EditText subjectName;
    private long subjectID;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_subject);
        myDBAdapter = new DBAdapter(this);
        myDBAdapter.open();
        Intent intent = getIntent();
        subjectName = (EditText) findViewById(R.id.edit_name);
        subjectName.setText(intent.getStringExtra(EX_SUBJECT));
        subjectID = Long.parseLong(intent.getStringExtra(EX_ID));
    }

    public void renameSubject(View view) {
        String newName = subjectName.getText().toString();
        myDBAdapter.updateSubject(subjectID, newName);
        myDBAdapter.close();
        this.finish();
    }

    public void cancel(View view) {
        myDBAdapter.close();
        this.finish();
    }
}