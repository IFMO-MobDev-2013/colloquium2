package com.example.colloquium2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Vector;

public class MarksActivity extends Activity implements IEventHadler
{
    public MarksAdapter adapter;

    private int id_subject = 0;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Vector<Mark> e = new Vector<Mark>();
        adapter = new MarksAdapter(this, e, this);
        ListView list_view = (ListView) findViewById(R.id.marksListView);
        list_view.setAdapter(adapter);

        id_subject = getIntent().getIntExtra("ID_SUBJECT", 0);
        Console.print("ID SUBJECT = "+id_subject);
        Cursor sth = Database.gi().query("select * from subjects where id_subject = "+id_subject);
        if (sth.moveToNext())
        {
            TextView tv = (TextView)findViewById(R.id.marksHeader);
            tv.setText(sth.getString(1));
        }
    }

    public void gotoAddMark(View v)
    {
        Intent intent = new Intent(this, MarkEditActivity.class);
        intent.putExtra("ID_SUBJECT", id_subject);
        startActivity(intent);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        adapter.marks.clear();
        int sum = 0;
        Cursor sth = Database.gi().query("select * from marks where id_subject = "+id_subject);
        while (sth.moveToNext())
        {
            Mark mark = new Mark(sth.getInt(0));
            mark.id_subject = sth.getInt(1);
            mark.title = sth.getString(2);
            mark.points = sth.getInt(3);
            sum += mark.points;
            adapter.marks.add(mark);
        }
        TextView tv = (TextView)findViewById(R.id.marksSum);
        tv.setText(sum+" points");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void handleEvent(Event e)
    {

    }
}
