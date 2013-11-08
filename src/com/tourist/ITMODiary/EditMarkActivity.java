package com.tourist.ITMODiary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditMarkActivity extends Activity {

    private static final String EDIT_FAILED = "Couldn't edit this mark: the mark should be an integer";

    private DBAdapter myDBAdapter;

    private EditText reasonEdit;
    private EditText markEdit;

    private String subject;
    private long subjectID;
    private long markID;
    private String reason;
    private long mark;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_mark);
        myDBAdapter = new DBAdapter(this);
        myDBAdapter.open();
        Intent intent = getIntent();
        subject = intent.getStringExtra(SubjectActivity.EX_SUBJECT);
        subjectID = Long.parseLong(intent.getStringExtra(SubjectActivity.EX_ID));
        markID = Long.parseLong(intent.getStringExtra(SubjectActivity.EX_MARK_ID));
        reason = intent.getStringExtra(SubjectActivity.EX_REASON);
        mark = Long.parseLong(intent.getStringExtra(SubjectActivity.EX_MARK));
        TextView editMarkText = (TextView) findViewById(R.id.edit_mark_text);
        editMarkText.setText(editMarkText.getText() + " " + subject);
        reasonEdit = (EditText) findViewById(R.id.edit_reason);
        markEdit = (EditText) findViewById(R.id.edit_mark);
        reasonEdit.setText(reason);
        markEdit.setText("" + mark);
    }

    public void editMark(View view) {
        try {
            String reason = reasonEdit.getText().toString();
            long mark = Long.parseLong(markEdit.getText().toString());
            myDBAdapter.updateMark(markID, subjectID, reason, mark);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, EDIT_FAILED, Toast.LENGTH_SHORT);
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