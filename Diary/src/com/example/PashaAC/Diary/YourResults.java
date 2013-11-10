package com.example.PashaAC.Diary;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class YourResults extends Activity {
    LessonsDataBase database;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> list = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parts_of_subject);
        listView = (ListView) findViewById(R.id.listView);
        database = new LessonsDataBase(this);
        arrayAdapter = new ArrayAdapter<String>(this,	R.layout.my_style, list);

        database.open();
        String name = getIntent().getStringExtra(MyActivity.KEY_ACTIVITY).toString();
        name = name.substring(0, name.indexOf("."));
        Cursor cursor = database.sqLiteDatabase.query(LessonsDataBase.TABLE_NAME, new String[] {
                LessonsDataBase.KEY_ID, LessonsDataBase.KEY_SUBJECT, LessonsDataBase.KEY_PART, LessonsDataBase.KEY_MARK},
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        while (cursor.moveToNext()) {
            // GET COLUMN INDICES + VALUES OF THOSE COLUMNS
            int id = cursor.getInt(cursor.getColumnIndex(LessonsDataBase.KEY_ID));
            String subject = cursor.getString(cursor.getColumnIndex(LessonsDataBase.KEY_SUBJECT));
            String part = cursor.getString(cursor.getColumnIndex(LessonsDataBase.KEY_PART));
            String mark = cursor.getString(cursor.getColumnIndex(LessonsDataBase.KEY_MARK));
            if (subject.equals(name) && part.equals("") == false) {
                list.add(part + ". " + mark);
            }
        }
        listView.setAdapter(arrayAdapter);
        database.close();
    }
}