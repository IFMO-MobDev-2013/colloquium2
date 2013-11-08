package com.tourist.ITMODiary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddMarkActivity extends Activity {

    private static final String ADD_FAILED = "Couldn't add a new mark: the mark should be an integer";

    private DBAdapter myDBAdapter;

    private EditText reasonEdit;
    private EditText markEdit;

    private String subject;
    private long subjectID;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_mark);
        myDBAdapter = new DBAdapter(this);
        myDBAdapter.open();
        Intent intent = getIntent();
        subject = intent.getStringExtra(SubjectActivity.EX_SUBJECT);
        subjectID = Long.parseLong(intent.getStringExtra(SubjectActivity.EX_ID));
        TextView addMarkText = (TextView) findViewById(R.id.add_mark_text);
        addMarkText.setText(addMarkText.getText() + " " + subject);
        reasonEdit = (EditText) findViewById(R.id.add_reason);
        markEdit = (EditText) findViewById(R.id.add_mark);
    }

    public void addMark(View view) {
        try {
            String reason = reasonEdit.getText().toString();
            long mark = Long.parseLong(markEdit.getText().toString());
            myDBAdapter.addMark(subjectID, reason, mark);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, ADD_FAILED, Toast.LENGTH_SHORT);
            toast.show();
        }
        myDBAdapter.close();
        this.finish();
    }

    public void cancel(View view) {
        myDBAdapter.close();
        this.finish();
    }
}