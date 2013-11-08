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
public class changeTaskActivity extends Activity {

    SingleSubjectDataBase mDbHelper;
    final public static int ADD = 0;
    final public static int UPDATE = 1;

    private String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_task);

        subject = getIntent().getExtras().getString("subject");
        mDbHelper = new SingleSubjectDataBase(this, subject);
        mDbHelper.open();

        final Button button = (Button) findViewById(R.id.newTaskButton);
        final EditText text = (EditText) findViewById(R.id.newTaskName);
        final EditText scoreText = (EditText) findViewById(R.id.scoreText);
        final TextView title = (TextView) findViewById(R.id.newTaskTitle);

        int type = getIntent().getExtras().getInt("type");

        if (type == 0){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (text.getText().toString().isEmpty()) return;
                    if (scoreText.getText().toString().isEmpty()){
                        scoreText.setText("0");
                    }
                    mDbHelper.addTask(text.getText().toString(), Integer.parseInt(scoreText.getText().toString()));
                    setResult(RESULT_OK, new Intent());
                    finish();
                }
            });
        } else {
            final String task = getIntent().getExtras().getString("task");
            final String score = getIntent().getExtras().getInt("score") + "";

            title.setText("Changing task: " + task);
            text.setText(task);
            scoreText.setText(score);
            scoreText.setSelection(0, score.length());

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (text.getText().toString().isEmpty()) return;
                    if (scoreText.getText().toString().isEmpty()){
                        scoreText.setText("0");
                    }
                    mDbHelper.updateTask(task, text.getText().toString(), Integer.parseInt(scoreText.getText().toString()));
                    setResult(RESULT_OK, new Intent());
                    finish();
                }
            });
        }
    }
}
