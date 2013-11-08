package ru.ifmo.Colloquim_2;

import android.app.Activity;
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
public class TaskList extends Activity {

    private SingleSubjectDataBase mDbHelper;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> tasks = new ArrayList<String>();
    private ArrayList<String> scores = new ArrayList<String>();
    private String subject;
    private int totalScore;

    private TextView title;


    public void updateList(){
        Cursor cursor = mDbHelper.getAllTasks();
        startManagingCursor(cursor);
        adapter.clear();
        tasks.clear();
        scores.clear();
        totalScore = 0;
        for (; cursor.moveToNext(); ){
            tasks.add(cursor.getString(cursor.getColumnIndex(SingleSubjectDataBase.KEY_TASK)));
            scores.add(cursor.getString(cursor.getColumnIndex(SingleSubjectDataBase.KEY_SCORE)));
            totalScore += cursor.getInt(cursor.getColumnIndex(SingleSubjectDataBase.KEY_SCORE));
            adapter.add(cursor.getString(cursor.getColumnIndex(SingleSubjectDataBase.KEY_TASK)) + "  (" + cursor.getString(cursor.getColumnIndex(SingleSubjectDataBase.KEY_SCORE)) + ")");
        }
        adapter.notifyDataSetChanged();
        title.setText(subject + " (" + totalScore + ")");

        MainDataBase subjDb = new MainDataBase(TaskList.this);
        subjDb.open();
        subjDb.updateSubj(subject, subject, totalScore);

        Intent broadcastIntent = new Intent(UpdateBroadcastReceiver.UpdateAction);
        sendBroadcast(broadcastIntent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list);

        subject = getIntent().getExtras().getString("subject");
        mDbHelper = new SingleSubjectDataBase(this, subject);
        mDbHelper.open();

        final ListView listView = (ListView) findViewById(R.id.taskListView);
        final Button addButton = (Button) findViewById(R.id.addTaskButton);
        title = (TextView) findViewById(R.id.TaskListTitle);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        listView.setAdapter(adapter);
        updateList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                Intent intent = new Intent(TaskList.this, changeTaskActivity.class);
                intent.putExtra("subject", subject);
                intent.putExtra("type", changeTaskActivity.UPDATE);
                intent.putExtra("task", tasks.get(position));
                intent.putExtra("score", scores.get(position));

                startActivityForResult(intent, 0);
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskList.this, changeTaskActivity.class);
                intent.putExtra("subject", subject);
                intent.putExtra("type", changeTaskActivity.ADD);

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
}
