package com.tourist.ITMODiary;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddSubjectActivity extends Activity {

    private DBAdapter myDBAdapter;

    private EditText subjectName;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_subject);
        myDBAdapter = new DBAdapter(this);
        myDBAdapter.open();
        subjectName = (EditText) findViewById(R.id.add_name);
    }

    public void addSubject(View view) {
        String name = subjectName.getText().toString();
        myDBAdapter.addSubject(name);
        myDBAdapter.close();
        this.finish();
    }

    public void cancel(View view) {
        myDBAdapter.close();
        this.finish();
    }
}
