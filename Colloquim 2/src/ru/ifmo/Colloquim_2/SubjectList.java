package ru.ifmo.Colloquim_2;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: asus
 * Date: 08.11.13
 * Time: 13:06
 * To change this template use File | Settings | File Templates.
 */
public class SubjectList extends Activity {

    MainDataBase mDbHelper;
    ArrayAdapter<String> adapter;
    ArrayList<String> subj = new ArrayList<String>();

    int totalScore;
    TextView scoreView;


    public void updateList(){
        Cursor cursor = mDbHelper.getAllSubjs();
        startManagingCursor(cursor);
        adapter.clear();
        subj.clear();
        totalScore = 0;
        for (; cursor.moveToNext(); ){
            subj.add(cursor.getString(cursor.getColumnIndex(MainDataBase.KEY_SUBJ)));
            totalScore += cursor.getInt(cursor.getColumnIndex(MainDataBase.KEY_SCORE));
            adapter.add(cursor.getString(cursor.getColumnIndex(MainDataBase.KEY_SUBJ)) + "  (" + cursor.getString(cursor.getColumnIndex(MainDataBase.KEY_SCORE)) + ")");
        }
        adapter.notifyDataSetChanged();
        scoreView.setText("Total score: " + totalScore);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_list);
        mDbHelper = new MainDataBase(this);
        mDbHelper.open();

        final ListView listView = (ListView) findViewById(R.id.subjectListView);
        final Button addButton = (Button) findViewById(R.id.addSubjectButton);
        scoreView = (TextView) findViewById(R.id.subjectScoreView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        listView.setAdapter(adapter);
        updateList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                Intent intent = new Intent(SubjectList.this, TaskList.class);
                intent.putExtra("subject", subj.get(position));
                startActivityForResult(intent, 0);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                Intent intent = new Intent(SubjectList.this, changeSubjectActivity.class);
                intent.putExtra("type", changeSubjectActivity.UPDATE);
                intent.putExtra("subject", subj.get(position));

                startActivityForResult(intent, 0);
                return true;
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SubjectList.this, changeSubjectActivity.class);
                intent.putExtra("type", changeSubjectActivity.ADD);

                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            updateList();
        }
    }

    public class UpdateBroadcastReceiver extends BroadcastReceiver {
        final static public String UpdateAction = "ru.ifmo.action.RssUpdate";

        @Override
        public void onReceive(Context context, Intent intent) {
            totalScore++;

        }
    }
}
