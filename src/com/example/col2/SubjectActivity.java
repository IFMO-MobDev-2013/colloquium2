package com.example.col2;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Александр
 * Date: 08.11.13
 * Time: 14:26
 * To change this template use File | Settings | File Templates.
 */
public class SubjectActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject);

        ArrayList<SubjectNode> items = new ArrayList<SubjectNode>();

        ListView listView = (ListView) findViewById(R.id.subject_list_view);

        final String name = getIntent().getStringExtra("name");

        String[] partsSubject = {"ДЗ",
                "Лабораторные",
                "Рубежка"};

        String[] scores = {"0", "0", "0"};

        for (int i = 0; i < partsSubject.length; i++) {
            items.add(new SubjectNode(partsSubject[i], scores[i]));
        }

        MarkDataBase markDataBase = new MarkDataBase(this);

        markDataBase.setArticle(name, items);

        SimpleAdapter adapter = new SimpleAdapter(SubjectActivity.this, markDataBase.getParts(name), R.layout.subject_list_view,
                new String[]{"partName", "partScore"},
                new int[] {R.id.part_name, R.id.part_score});

        listView.setAdapter(adapter);

    }
}
