package com.example.Diary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

public class SubjectActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private myDbHelper mDbHelper = null;
    private SimpleCursorAdapter items = null;
    private ListView lv = null;

    public static final String KEY_SUBJ = "subject";



    private static final int INSERT_ID = Menu.FIRST;
    private static final int DROP_ID = Menu.FIRST + 1;
    private static final int DELETE_ID = Menu.FIRST + 2;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subjects);

        mDbHelper = new myDbHelper(this);


        lv = (ListView) findViewById(R.id.listView);
        registerForContextMenu(lv);
        fill();





        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SubjectActivity.this, TasksActivity.class);
                Cursor cursor = (Cursor) items.getItem(i);
                String name = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.KEY_SNAME));

                intent.putExtra(KEY_SUBJ, name);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.menu_insert);
        menu.add(0, DROP_ID, 0, R.string.menu_drop);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case INSERT_ID:
                createSubject();
                return true;
            case DROP_ID:
                AlertDialog.Builder a = new AlertDialog.Builder(this);
                a.setTitle(R.string.sure);

                a.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mDbHelper.open();
                        //mDbHelper.dropSubjects();
                        mDbHelper.dropAll();
                        mDbHelper.close();
                        fill();
                    }
                });

                a.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                a.show();

        }

        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                mDbHelper.open();
                mDbHelper.deleteSubj(info.id);
                mDbHelper.close();
                fill();
                items.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void fill() {
        Cursor cursor = null;

        try {
            mDbHelper.open();
            cursor = mDbHelper.fetchAllSubj();

        } catch (Exception e) {
            e.printStackTrace();
        }


        String[] from = new String[]{myDbHelper.KEY_SNAME, myDbHelper.KEY_SUM};
        int[] to = new int[]{R.id.text1, R.id.text2};

        try {
            items = new SimpleCursorAdapter(this, R.layout.tasks_row, cursor, from, to);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            lv.setAdapter(items);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mDbHelper.close();

    }


    private void createSubject() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(R.string.menu_insert);
        alert.setMessage(R.string.subjects_name);

        final EditText input = new EditText(this);

        alert.setView(input);


        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String t = input.getText().toString();

                int pos = t.indexOf(",");
                if (pos < 0 || pos >= t.length() - 1) {
                    Toast.makeText(SubjectActivity.this, "invalid format", 1000).show();
                    return;
                }

                String name = t.substring(0, pos);
                String points = t.substring(pos + 1, t.length());
                points = points.replaceAll(" ", "");
                Integer n = Integer.parseInt(points);

                try {
                    mDbHelper.open();
                    mDbHelper.createSubject(name, n);
                    mDbHelper.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                fill();
            }
        });

        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }
}
