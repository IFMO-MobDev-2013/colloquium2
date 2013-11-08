package com.ifmomd.igushkin.colloquium2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

/**
 * Created by Sergey on 11/8/13.
 */
public class PointsActivity extends Activity implements AdapterView.OnItemClickListener {
    PointsDBAdapter mDb;
    long            subjectId;

    ListView lstPoints;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        lstPoints = (ListView) findViewById(R.id.lstList);
        subjectId = getIntent().getLongExtra(SubjectsActivity.EXTRA_SUBJECT_ID, -1);
        mDb = new PointsDBAdapter(this);
        mDb.open();
        updateList();
        lstPoints.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.subjects_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public static final int REQUEST_ADD_POINTS = 1337;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mnuAdd) {
            Intent i = new Intent(this, AddPointsActivity.class);
            startActivityForResult(i, REQUEST_ADD_POINTS);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADD_POINTS && resultCode == RESULT_OK) {
            String description = data.getStringExtra(AddPointsActivity.EXTRA_DESCRIPTION);
            int points = data.getIntExtra(AddPointsActivity.EXTRA_POINTS, 0);
            mDb.createPoints(subjectId, description, points);
        }
        if (requestCode == REQUEST_EDIT_POINTS && resultCode == RESULT_OK) {
            long id = data.getLongExtra(SubjectsActivity.EXTRA_SUBJECT_ID, -1);
            if (data.hasExtra(AddSubjectActivity.EXTRA_DELETE)) {
                mDb.deletePointsById(id);
            } else {
                String description = data.getStringExtra(AddPointsActivity.EXTRA_DESCRIPTION);
                int points = data.getIntExtra(AddPointsActivity.EXTRA_POINTS, 0);
                mDb.updatePointsInfo(id, description, points);
            }
        }
        updateList();
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static final int REQUEST_EDIT_POINTS = 878;

    private void updateList() {
        Cursor c = mDb.fetchPointsBySubject(subjectId);
        startManagingCursor(c);
        lstPoints.setAdapter(new SimpleCursorAdapter(this, R.layout.lst_left_right_item, c,
                                                     new String[]{PointsDBAdapter.KEY_DESCRIPTION, PointsDBAdapter.KEY_POINTS},
                                                     new int[]{android.R.id.text1, android.R.id.text2}));
        c = mDb.fetchSubjectById(subjectId);
        c.moveToFirst();
        String name = c.getString(c.getColumnIndex(PointsDBAdapter.KEY_SUBJECT));
        int points = c.getInt(c.getColumnIndex(PointsDBAdapter.KEY_POINTS_SUM));
        setTitle(name + " (" + points + " points)");
        c.close();
    }

    long lastClickedId = -1;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (lastClickedId == id) {
            Intent i = new Intent(this, AddPointsActivity.class);
            i.putExtra(SubjectsActivity.EXTRA_SUBJECT_ID, id);
            i.putExtra(AddPointsActivity.EXTRA_DESCRIPTION, ((TextView)view.findViewById(android.R.id.text1)).getText().toString());
            i.putExtra(AddPointsActivity.EXTRA_POINTS, Integer.parseInt(((TextView)view.findViewById(android.R.id.text2)).getText().toString()));
            startActivityForResult(i, REQUEST_EDIT_POINTS);
            updateList();
        } else {
            lastClickedId = id;
            Toast.makeText(this, getString(R.string.tstTapAgainToDelete), Toast.LENGTH_SHORT).show();
        }
    }
}