package com.example.colloquium2;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MarkEditActivity extends Activity
{
    private Mark current = null;
    private Subject subject = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.markedit);

        current = new Mark(getIntent().getIntExtra("ID_MARK", 0));
        subject = new Subject(getIntent().getIntExtra("ID_SUBJECT", 0));

        if (current.id_mark != 0)
        {
            Cursor sth = Database.gi().query("select * from marks where id_mark = "+ current.id_mark);
            sth.moveToNext();
            current.id_subject = Integer.parseInt(sth.getString(1));
            current.title = sth.getString(2);
            current.points = Integer.parseInt(sth.getString(3));

            EditText name = (EditText)findViewById(R.id.editMarkTitle);
            name.setText(current.title);
            EditText points = (EditText)findViewById(R.id.editMarkPoints);
            points.setText(current.points+"");

            ((Button)findViewById(R.id.deleteButton)).setEnabled(true);
        }
        else
        {
            current.id_subject = subject.id_subject;
            ((Button)findViewById(R.id.deleteButton)).setEnabled(false);
        }
    }

    public void onSaveMark(View v)
    {
        EditText name = (EditText)findViewById(R.id.editMarkTitle);
        EditText points = (EditText)findViewById(R.id.editMarkPoints);
        current.title = name.getText().toString();
        current.points = Integer.parseInt(points.getText().toString());

        if (current.id_mark == 0)
        {
            Console.print("Current id_subject = "+current.id_subject);
            Database.gi().exec("insert into marks values(null, "+current.id_subject+", '" + current.title + "', " + current.points + ")");
            Toast t = Toast.makeText(this, "Mark is added", 3000);
            t.show();
            finish();
        }
        else
        {
            Database.gi().exec("update marks set title = '" + current.title+ "', points = " + current.points + " where id_mark = "+ current.id_mark);
            Toast t = Toast.makeText(this, "Mark is updated", 3000);
            t.show();
        }
    }

    public void onDeleteMark(View v)
    {
        Database.gi().exec("delete from marks where id_mark = " + current.id_mark);
        Toast t = Toast.makeText(this, "Mark is deleted", 3000);
        t.show();
        finish();
    }
}
