package com.example.colloquium2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.example.colloquium2.db.CourseDataSource;
import com.example.colloquium2.db.GradesDataSource;

public class ViewCourseGradesActivity extends Activity {
    GradesDataSource source = new GradesDataSource(this);
    SimpleCursorAdapter adapter;
    ListView listView;
    int course_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course);
        source.open();

        Intent intent = getIntent();

        course_id = intent.getIntExtra("course_id", -1);
        if (course_id == -1) {
            Log.e("Recordbook", "ViewCourseGrades intent with no extra");
            finish();
        }

        Cursor gradeList = source.getGrades(course_id);
        listView = (ListView)findViewById(R.id.courseList);
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, gradeList, new String[] {"name", "grade"}, new int[] {android.R.id.text1, android.R.id.text2}, 0);

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
        Cursor courseList = source.getGrades(course_id);
        adapter.swapCursor(courseList);
        adapter.notifyDataSetChanged();
    }

    public void addClick(View view) {
        EditText gradeNameText = (EditText)findViewById(R.id.gradeName);
        EditText gradeValueText = (EditText)findViewById(R.id.gradeValue);

        Log.d("COL2", "addClick clicked");
        if (gradeNameText.getText().length() == 0 || gradeValueText.getText().length() == 0) return;
        try {
            int value = Integer.parseInt(gradeValueText.getText().toString());
            source.addGrade(gradeNameText.getText().toString(), course_id, value);
        } catch (NumberFormatException f) {
            return;
        }

        update();
    }
}
