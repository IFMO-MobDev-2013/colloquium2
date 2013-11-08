package com.example.colloquium2;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SubjectEditActivity extends Activity
{
    private int id_subject = 0;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);

        id_subject = getIntent().getIntExtra("ID_SUBJECT", 0);

        if (id_subject != 0)
        {
            Cursor c = Database.gi().query("select * from subjects where id_subject = "+ id_subject);
            c.moveToNext();
            EditText name = (EditText)findViewById(R.id.editSubjectName);
            name.setText(c.getString(1));
            ((Button)findViewById(R.id.deleteButton)).setEnabled(true);
        }
        else
        {
            ((Button)findViewById(R.id.deleteButton)).setEnabled(false);
        }
    }

    public void onSaveButtonClicked(View v)
    {
        EditText name = (EditText)findViewById(R.id.editSubjectName);

        Cursor c = Database.gi().query("select * from subjects where id_subject = "+ id_subject);
        if (c.getCount() == 0)
        {
            Database.gi().exec("insert into subjects values(null,'" + (name.getText().toString()) + "')");
            Toast t = Toast.makeText(this, "New subject is added", 3000);
            t.show();
            c.close();
            finish();
        }
        else
        {
            Database.gi().exec("update subjects set name = '"+(name.getText().toString())+"' where id_subject = "+ id_subject);
            Toast t = Toast.makeText(this, "Subject is updated", 3000);
            t.show();
            c.close();
        }
    }

    public void onDeleteButtonClicked(View v)
    {
        Database.gi().exec("delete from marks where id_subject = " + id_subject);
        Database.gi().exec("delete from subjects where id_subject = "+ id_subject);
        Toast t = Toast.makeText(this, "Subject is deleted", 3000);
        t.show();
        finish();
    }
}
