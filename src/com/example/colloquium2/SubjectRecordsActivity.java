package com.example.colloquium2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

/**
 * Created with IntelliJ IDEA.
 * User: Genyaz
 * Date: 08.11.13
 * Time: 13:10
 * To change this template use File | Settings | File Templates.
 */
public class SubjectRecordsActivity extends Activity {

    private TextView subjectInfo;
    private ListView subjectRecordsList;
    private SubjectRecordsAdapter subjectRecordsAdapter;
    private Context context;
    private SQLiteDatabase rdb;
    private int subjectId;

    public class SubjectRecordsAdapter extends BaseAdapter {

        private Vector<SubjectRecordView> subjectRecordViews;

        public class SubjectRecordView {
            public View view;
            public String name;
            public int score;
            public int id;

            public SubjectRecordView(String name, int score, int id) {
                TextView textView = new TextView(context);
                textView.setText(name + ": " + score);
                textView.setTextSize(20);
                view = textView;
                this.name = name;
                this.score = score;
                this.id = id;
            }
        }

        public SubjectRecordsAdapter() {
            subjectRecordViews = new Vector<SubjectRecordView>();
        }

        public void addSubjectRecord(String subjectRecordName, int subjectRecordScore, int subjectRecordId) {
            subjectRecordViews.add(new SubjectRecordView(subjectRecordName, subjectRecordScore, subjectRecordId));
            notifyDataSetChanged();
        }

        public int getSubjectRecordId(int position) {
            return subjectRecordViews.get(position).id;
        }

        @Override
        public int getCount() {
            return subjectRecordViews.size();
        }

        @Override
        public Object getItem(int position) {
            return subjectRecordViews.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return subjectRecordViews.get(position).view;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_records);
        context = this;
        subjectId = getIntent().getIntExtra(getString(R.string.subject_id), -1);
        subjectRecordsList = (ListView) findViewById(R.id.subjectRecordsList);
        subjectInfo = (TextView) findViewById(R.id.subject_info);
        subjectRecordsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, SubjectRecordModifyActivity.class);
                intent.putExtra(getString(R.string.record_id), subjectRecordsAdapter.getSubjectRecordId(position));
                intent.putExtra(getString(R.string.subject_id), subjectId);
                startActivity(intent);
                return true;
            }
        });
    }

    public void onAddRecordClick(View view) {
        Intent intent = new Intent(this, SubjectRecordModifyActivity.class);
        intent.putExtra(getString(R.string.record_id), -1);
        intent.putExtra(getString(R.string.subject_id), subjectId);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    public void refreshList() {
        SubjectsDatabase sdb = new SubjectsDatabase(this);
        rdb = sdb.getReadableDatabase();
        Cursor cursor = rdb.query(SubjectsDatabase.DATABASE_NAME, null,
                SubjectsDatabase._ID + " = " + subjectId, null, null, null, null, "1");
        String subjectName = "";
        while (cursor.moveToNext()) {
            subjectName = cursor.getString(cursor.getColumnIndex(SubjectsDatabase.SUBJECT_NAME));
        }
        cursor.close();
        rdb.close();
        sdb.close();
        subjectInfo.setText(subjectName);
        subjectRecordsAdapter = new SubjectRecordsAdapter();
        subjectRecordsList.setAdapter(subjectRecordsAdapter);
        SubjectRecordsDatabase srdb = new SubjectRecordsDatabase(this);
        rdb = srdb.getReadableDatabase();
        cursor = rdb.query(SubjectRecordsDatabase.DATABASE_NAME, null,
                SubjectRecordsDatabase.SUBJECT_ID + " = " + subjectId, null, null, null, null, "100");
        int recordNameIndex = cursor.getColumnIndex(SubjectRecordsDatabase.RECORD_NAME);
        int idIndex = cursor.getColumnIndex(SubjectRecordsDatabase._ID);
        int scoreIndex = cursor.getColumnIndex(SubjectRecordsDatabase.RECORD_POINTS);
        String recordName;
        int id;
        int score;
        int totalScore = 0;
        while (cursor.moveToNext()) {
            recordName = cursor.getString(recordNameIndex);
            id = cursor.getInt(idIndex);
            score = cursor.getInt(scoreIndex);
            totalScore += score;
            subjectRecordsAdapter.addSubjectRecord(recordName, score, id);
        }
        subjectInfo.append(": " + totalScore);
        cursor.close();
        rdb.close();
        srdb.close();
    }
}