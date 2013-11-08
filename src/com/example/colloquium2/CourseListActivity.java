package com.example.colloquium2;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.example.colloquium2.db.CourseDataSource;

public class CourseListActivity extends Activity {
    CourseDataSource source = new CourseDataSource(this);
    SimpleCursorAdapter adapter;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        source.open();

        Cursor courseList = source.getAllCourses();
        listView = (ListView)findViewById(R.id.courseList);
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, courseList, new String[] {"name", "grade"}, new int[] {android.R.id.text1, android.R.id.text2}, 0);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                source.deleteCourse(id);
                update();
            }
        });

        update();
    }

    private void update() {
        Cursor courseList = source.getAllCourses();

        adapter.swapCursor(courseList);
        adapter.notifyDataSetChanged();
    }

    public void addClick(View view) {
        EditText addText = (EditText)findViewById(R.id.addName);
        Log.d("COL2", "addClick clicked");
        if (addText.getText().length() == 0) return;
        source.createCourse(addText.getText().toString());

        update();
        addText.getText().clear();
    }
}
