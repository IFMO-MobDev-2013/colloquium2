package ru.ifmo.ctddev.skripnikov.colloquium2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class RatingsListActivity extends Activity {
    private ListView listView;
    private TextView textView;
    private Subject subject;
    private AddRatingDialog ard;
    private EditRatingDialog erd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ratings_list_activity);
        subject = (Subject) getIntent().getSerializableExtra("subject");
        listView = (ListView) findViewById(R.id.ratings_list_activity_lv);
        textView = (TextView) findViewById(R.id.ratings_list_activity_rating);
        reloadRatingsList();
    }

    public void reloadRatingsList() {
        DBStorage dbStorage = new DBStorage(this);
        final Rating[] ratings = dbStorage.getRatingsBySubjectId(subject.id);
        dbStorage.destroy();
        textView.setText((new Integer(subject.rating)).toString());
        RatingsListAdapter adapter = new RatingsListAdapter(getBaseContext(), ratings);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                erd = new EditRatingDialog(getBaseContext(), subject, ratings[position], new Listener() {
                    @Override
                    public void onDialogDismissed() {
                        reloadRatingsList();
                    }
                });
                erd.show(getFragmentManager(), "erd");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.subjects_list_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.subjects_list_activity_action_add:
                ard = new AddRatingDialog(getBaseContext(), subject, new Listener() {
                    @Override
                    public void onDialogDismissed() {
                        reloadRatingsList();
                    }
                });
                ard.show(getFragmentManager(), "asd");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
