package ru.georgeee.android.colloquium2.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import ru.georgeee.android.colloquium2.R;
import ru.georgeee.android.colloquium2.db.SubjectTable;
import ru.georgeee.android.colloquium2.model.Subject;

import java.io.IOException;

public class EditSubjectActivity extends Activity {


    Subject subject;
    Button backBtn;
    Button saveBtn;
    EditText subjectName;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_subject);
        subjectName = (EditText) findViewById(R.id.editSubjectSubjectName);
        backBtn = (Button) findViewById(R.id.backBtn);
        Bundle extras = getIntent().getExtras();
        subject = (Subject) extras.get("subject");

        getWindow().setTitle(subject.getName());

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        subjectName.setText(subject.getName());
        saveBtn = (Button) findViewById(R.id.editSubjectSaveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject.setName(subjectName.getText().toString());
                try {
                    SubjectTable.getInstance(v.getContext()).save(subject);
                } catch (IOException e) {
                    Log.e(getClass().getCanonicalName(), e.toString());
                }
                getIntent().putExtra("subject", subject);
                setResult(RESULT_OK, getIntent());
                finish();
            }
        });
    }


}
