package ru.ifmo.Colloquim_2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: asus
 * Date: 08.11.13
 * Time: 13:24
 * To change this template use File | Settings | File Templates.
 */
public class changeSubjectActivity extends Activity {

    MainDataBase mDbHelper;
    final public static int ADD = 0;
    final public static int UPDATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_subject);
        mDbHelper = new MainDataBase(this);
        mDbHelper.open();

        final Button button = (Button) findViewById(R.id.newSubjectButton);
        final EditText text = (EditText) findViewById(R.id.newSubjectName);
        final TextView title = (TextView) findViewById(R.id.newSubjectTitle);

        int type = getIntent().getExtras().getInt("type");

        if (type == 0){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (text.getText().toString().isEmpty()) return;
                    mDbHelper.addSubj(text.getText().toString(), 0);
                    setResult(RESULT_OK, new Intent());
                    finish();
                }
            });
        } else {
            final String subject = getIntent().getExtras().getString("subject");

            title.setText("Changing subject: " + subject);
            text.setText(subject);
            text.setSelection(0, subject.length());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (text.getText().toString().isEmpty()) return;
                    mDbHelper.updateSubj(subject, text.getText().toString(), 0);
                    setResult(RESULT_OK, new Intent());
                    finish();
                }
            });
        }
    }
}
