package com.ifmomd.igushkin.colloquium2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import org.w3c.dom.Text;

public class SubjectsActivity extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    /**
     * Called when the activity is first created.
     */

    PointsDBAdapter mDb;
    ListView        lstSubjects;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.subjects_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public static final int REQUEST_ADD = 157;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mnuAdd) {
            Intent i = new Intent(this, AddSubjectActivity.class);
            startActivityForResult(i, REQUEST_ADD);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADD && resultCode == RESULT_OK) {
            mDb.createSubject(data.getStringExtra(AddSubjectActivity.EXTRA_SUBJECT_NAME));
        }
        if (requestCode == REQUEST_EDIT_SUBJECT && resultCode == RESULT_OK) {
            long id = data.getLongExtra(EXTRA_SUBJECT_ID, -1);
            if (data.hasExtra(AddSubjectActivity.EXTRA_DELETE))
                mDb.deleteSubjectById(data.getLongExtra(EXTRA_SUBJECT_ID, -1));
            else {
                String newName = data.getStringExtra(AddSubjectActivity.EXTRA_SUBJECT_NAME);
                mDb.updateSubjectInfo(id, newName);
            }
        }
        updateList();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        mDb = new PointsDBAdapter(this);
        mDb.open();
        lstSubjects = (ListView) findViewById(R.id.lstList);
        updateList();
        lstSubjects.setOnItemClickListener(this);
        lstSubjects.setOnItemLongClickListener(this);
    }

    private void updateList() {
        Cursor c = mDb.fetchAllSubjects();
        c.moveToFirst();
        startManagingCursor(c);
        String[] from = new String[]{PointsDBAdapter.KEY_SUBJECT, PointsDBAdapter.KEY_POINTS_SUM};
        int[] to = new int[]{android.R.id.text1, android.R.id.text2};
        c.moveToFirst();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.lst_left_right_item, c, from, to);
        lstSubjects.setAdapter(adapter);
    }

    public static final String EXTRA_SUBJECT_ID = "subject id";

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this, PointsActivity.class);
        i.putExtra(EXTRA_SUBJECT_ID, id);
        startActivity(i);
    }

    public static final int REQUEST_EDIT_SUBJECT = 2399;

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this, AddSubjectActivity.class);
        i.putExtra(EXTRA_SUBJECT_ID, id);
        i.putExtra(AddSubjectActivity.EXTRA_SUBJECT_NAME, ((TextView)view.findViewById(android.R.id.text1)).getText().toString());
        startActivityForResult(i, REQUEST_EDIT_SUBJECT);
        return true;
    }
}
