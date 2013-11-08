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

public class SubjectsListActivity extends Activity {

    private ListView listView;
    private AddSubjectDialog asd;
    private EditSubjectDialog esd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subjects_list_activity);
        listView = (ListView) findViewById(R.id.subjects_list_activity_lv);
        reloadSubjectsList();
    }

    public void onResume() {
        super.onPause();
        reloadSubjectsList();
    }

    private void reloadSubjectsList() {
        DBStorage dbStorage = new DBStorage(getBaseContext());
        final Subject[] subject = dbStorage.getSubjects();
        dbStorage.destroy();

        SubjectsListAdapter adapter = new SubjectsListAdapter(getBaseContext(), subject);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                Intent intent = new Intent(getBaseContext(), RatingsListActivity.class);
                intent.putExtra("subject", subject[position]);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                esd = new EditSubjectDialog(getBaseContext(),subject[position], new Listener() {
                    @Override
                    public void onDialogDismissed() {
                        reloadSubjectsList();
                    }
                });
                esd.show(getFragmentManager(), "esd");
                return false;
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
                asd = new AddSubjectDialog(getBaseContext(), new Listener() {
                    @Override
                    public void onDialogDismissed() {
                        reloadSubjectsList();
                    }
                });
                asd.show(getFragmentManager(), "asd");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
