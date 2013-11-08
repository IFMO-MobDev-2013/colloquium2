package ru.georgeee.android.colloquium2.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import ru.georgeee.android.colloquium2.R;
import ru.georgeee.android.colloquium2.db.MarkTable;
import ru.georgeee.android.colloquium2.model.Mark;
import ru.georgeee.android.colloquium2.model.Subject;

import java.io.IOException;

public class EditMarkActivity extends Activity {


    Subject subject;
    Mark mark;
    Button backBtn;
    Button saveBtn;
    TextView subjectName;
    EditText markName;
    EditText markValue;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_mark);
        subjectName = (TextView) findViewById(R.id.editMarkSubjectName);
        markName = (EditText) findViewById(R.id.editMarkName);
        markValue = (EditText) findViewById(R.id.editMarkValue);
        backBtn = (Button) findViewById(R.id.backBtn);
        Bundle extras = getIntent().getExtras();
        subject = (Subject) extras.get("subject");
        mark = (Mark) extras.get("mark");

        getWindow().setTitle(subject.getName() + " / " + mark.getName());

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        markName.setText(mark.getName());
        subjectName.setText(subject.getName());
        markValue.setText(mark.getValue());
        saveBtn = (Button) findViewById(R.id.editMarkSaveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mark.setName(markName.getText().toString());
                try{
                    mark.setValue(Integer.valueOf(markValue.getText().toString()));
                }catch(NumberFormatException ex){
                    Log.e(getClass().getCanonicalName(), ex.toString());
                }
                try {
                    MarkTable.getInstance(v.getContext()).save(mark);
                } catch (IOException e) {
                    Log.e(getClass().getCanonicalName(), e.toString());
                }
                getIntent().putExtra("mark", mark);
                setResult(RESULT_OK, getIntent());
                finish();
            }
        });
    }


}
