package com.example.colloquium2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.colloquium2.databases.SubjectRecordsDatabase;
import com.example.colloquium2.databases.SubjectsDatabase;

import java.util.Vector;

public class SubjectsActivity extends Activity
{
    private ListView subjectsList;
    private SubjectsAdapter subjectsAdapter;
    private Context context;
    private SQLiteDatabase rdb;

    public class SubjectsAdapter extends BaseAdapter {

        private Vector<SubjectView> subjectViews;

        public class SubjectView {
            public View view;
            public String name;
            public int id;

            public SubjectView(String name, int id) {
                TextView textView = new TextView(context);
                textView.setText(name);
                textView.setTextSize(20);
                view = textView;
                this.name = name;
                this.id = id;
            }
        }

        public SubjectsAdapter() {
            subjectViews = new Vector<SubjectView>();
        }

        public void addSubject(String subjectName, int subjectId) {
            subjectViews.add(new SubjectView(subjectName, subjectId));
            notifyDataSetChanged();
        }

        public int getSubjectId(int position) {
            return subjectViews.get(position).id;
        }

        @Override
        public int getCount() {
            return subjectViews.size();
        }

        @Override
        public Object getItem(int position) {
            return subjectViews.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return subjectViews.get(position).view;
        }
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subjects);
        context = this;
        subjectsList = (ListView) findViewById(R.id.subjectsList);
        subjectsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, SubjectRecordsActivity.class);
                intent.putExtra(getString(R.string.subject_id), subjectsAdapter.getSubjectId(position));
                startActivity(intent);
            }
        });
        subjectsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, SubjectModifyActivity.class);
                intent.putExtra(getString(R.string.subject_id), subjectsAdapter.getSubjectId(position));
                startActivity(intent);
                return true;
            }
        });
        refreshList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    public void refreshList() {
        context = this;
        SQLiteOpenHelper db = new SubjectsDatabase(this);
        rdb = db.getReadableDatabase();
        subjectsAdapter = new SubjectsAdapter();
        subjectsList.setAdapter(subjectsAdapter);
        Cursor cursor = rdb.query(SubjectsDatabase.DATABASE_NAME,
                null, null, null, null, null, null);
        int name_column = cursor.getColumnIndex(SubjectsDatabase.SUBJECT_NAME);
        int id_column = cursor.getColumnIndex(SubjectsDatabase._ID);
        String name;
        int id;
        while (cursor.moveToNext()) {
            name = cursor.getString(name_column);
            id = cursor.getInt(id_column);
            subjectsAdapter.addSubject(name, id);
        }
        cursor.close();
        rdb.close();
        db.close();
    }

    public void onAddSubjectClick(View view) {
        Intent intent = new Intent(this, SubjectModifyActivity.class);
        intent.putExtra(getString(R.string.subject_id), -1);
        startActivity(intent);
    }
}
