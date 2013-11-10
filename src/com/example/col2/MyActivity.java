package com.example.col2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MyActivity extends Activity {
    DB db;
    ListView lv;
    ListView lvMark;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        lv = (ListView) findViewById(R.id.lvMain);
        lvMark = (ListView) findViewById(R.id.lvMark);
        db = new DB(this);
        Cursor cursor = db.getObs();
        startManagingCursor(cursor);
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(
                this, R.layout.simplerow, cursor, new String[]{"name"}, new int[]{R.id.textView}, 0);
        lv.setAdapter(simpleCursorAdapter);

        Cursor cursort = db.getObs();
        startManagingCursor(cursort);
        SimpleCursorAdapter simpleCursorAdaptert = new SimpleCursorAdapter(
                this, R.layout.simplerow, cursort, new String[]{"mark"}, new int[]{R.id.textView}, 0);
        lvMark.setAdapter(simpleCursorAdaptert);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyActivity.this, ActivityEdit.class);
                String st = db.getObById(id);
                intent.putExtra("st", id);
                startActivityForResult(intent, 0);
            }
        });

        Button btnAdd = (Button) findViewById(R.id.btnAddMain);
        View.OnClickListener oclBtnAdd = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivity.this, ActivityAdd.class);
                startActivityForResult(intent, 0);
            }
        };
        btnAdd.setOnClickListener(oclBtnAdd);



    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Cursor c = db.getObs();
        startManagingCursor(c);
        SimpleCursorAdapter s = new SimpleCursorAdapter(
                this, R.layout.simplerow, c, new String[]{"name"}, new int[]{R.id.textView}, 0);
        lv.setAdapter(s);
        Cursor ct = db.getObs();
        startManagingCursor(ct);
        SimpleCursorAdapter st = new SimpleCursorAdapter(
                this, R.layout.simplerow, ct, new String[]{"mark"}, new int[]{R.id.textView}, 0);
        lvMark.setAdapter(st);


    }
}
