package com.example.col2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class StartActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ListView listView = (ListView) findViewById(R.id.start_list_view);

        MarkDataBase markDataBase = new MarkDataBase(this);


        String[] subjectNames = {"Андроид",
                "Английский",
                "C++"};

        String[] scores = {"0", "0", "0"};

        for (int i = 0; i < subjectNames.length; i++) {
            markDataBase.addTable(subjectNames[i], scores[i]);
        }

        SimpleAdapter adapter = new SimpleAdapter(StartActivity.this, markDataBase.getSubjectItems(), R.layout.start_list_view,
                new String[]{"subjectName", "score"},
                new int[] {R.id.subject_name, R.id.score});

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View view, int index, long arg3) {

                MarkDataBase markDataBase = new MarkDataBase(StartActivity.this);

                String name = markDataBase.getName(index);

                Intent intent = new Intent(StartActivity.this, SubjectActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
    }
}
