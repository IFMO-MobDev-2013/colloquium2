package com.example.PashaAC.Diary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MyActivity extends Activity {
    public static final String KEY_ACTIVITY = "key";
    ArrayList<String> subjects = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;
    ListView listView, listViewMarks;
    LessonsDataBase database;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        database = new LessonsDataBase(this);
        listView = (ListView) findViewById(R.id.listView);
        Button add = (Button) findViewById(R.id.button);

        database.open();
        database.deleteAllItem();
        database.insertSubject("Математика", "", 0);
        database.insertSubject("Математика", "Домашняя работа №1", 10);
        database.insertSubject("Математика", "Домашняя работа №2", 8);
        database.insertSubject("Математика", "Колоквиум", 15);
        database.insertSubject("Русский язык", "", 0);
        database.insertSubject("Русский язык", "Домашняя работа №1", 10);
        database.insertSubject("Русский язык", "Домашняя работа №2", 8);
        database.insertSubject("Русский язык", "Колоквиум", 15);
        readFromDateBase();
        arrayAdapter = new ArrayAdapter<String>(this,	R.layout.my_style, subjects);
        listView.setAdapter(arrayAdapter);
        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long number) {
                Intent intent = new Intent(MyActivity.this, YourResults.class);
                intent.putExtra(KEY_ACTIVITY, subjects.get(index));
                intent.putExtra(KEY_ACTIVITY, subjects.get(index));
                database.close();
                startActivity(intent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            EditText inputText = (EditText) findViewById(R.id.editText);
            @Override
            public void onClick(View view) {
                String subject = inputText.getText().toString();
                database.insertSubject(subject, "", 0);
           }
        });
    }

    public static final int IDM_RENAME = 101;
    public static final int IDM_DELETE = 102;
    int pozition = 0;
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo aMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
        pozition = aMenuInfo.position;
        menu.add(Menu.NONE, IDM_RENAME, Menu.NONE, "Rename");
        menu.add(Menu.NONE, IDM_DELETE, Menu.NONE, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        EditText editText;
        Button button;
        InputMethodManager imm;
        switch (item.getItemId())
        {
            case IDM_RENAME:
                editText = (EditText) findViewById(R.id.editText);
                editText.setHint("Enter new subject name");
                button = (Button) findViewById(R.id.button);
                button.setText("Rename");
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, 0);
                button.setOnClickListener(new View.OnClickListener() {
                    EditText editText = (EditText) findViewById(R.id.editText);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    Button button = (Button) findViewById(R.id.button);
                    @Override
                    public void onClick(View v) {
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        rename(subjects.get(pozition), editText.getText().toString());
                        subjects.set(pozition, editText.getText().toString() + ". " + subjects.get(pozition).substring(subjects.get(pozition).indexOf(".") + 1,  subjects.get(pozition).length()));
                        button.setText("Add");
                        editText.setText("");
                        editText.setHint("Enter your new subject");
                    }
                });
                break;
            case IDM_DELETE:
                deleteSubject(subjects.get(pozition));
                arrayAdapter = new ArrayAdapter<String>(this,	R.layout.my_style, subjects);
                listView.setAdapter(arrayAdapter);
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    private void rename(String str1, String str2) {
        subjects = new ArrayList<String>();
        str1 = str1.substring(0, str1.indexOf("."));
        Cursor cursor = database.sqLiteDatabase.query(LessonsDataBase.TABLE_NAME, new String[] {
                LessonsDataBase.KEY_ID, LessonsDataBase.KEY_SUBJECT, LessonsDataBase.KEY_PART, LessonsDataBase.KEY_MARK},
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        while (cursor.moveToNext()) {
            // GET COLUMN INDICES + VALUES OF THOSE COLUMNS
            int id = cursor.getInt(cursor.getColumnIndex(LessonsDataBase.KEY_ID));
            String subject = cursor.getString(cursor.getColumnIndex(LessonsDataBase.KEY_SUBJECT));
            String part = cursor.getString(cursor.getColumnIndex(LessonsDataBase.KEY_PART));
            String mark = cursor.getString(cursor.getColumnIndex(LessonsDataBase.KEY_MARK));
            if (subject.equals(str1)) {
                database.updateItem(id, str2, part, Integer.parseInt(mark));
            }
        }


        arrayAdapter = new ArrayAdapter<String>(this,   R.layout.my_style, subjects);
        listView.setAdapter(arrayAdapter);
        cursor.close();
    }

    private void deleteSubject(String name) {
        subjects = new ArrayList<String>();
        name = name.substring(0, name.indexOf("."));
        Cursor cursor = database.sqLiteDatabase.query(LessonsDataBase.TABLE_NAME, new String[] {
                LessonsDataBase.KEY_ID, LessonsDataBase.KEY_SUBJECT, LessonsDataBase.KEY_PART, LessonsDataBase.KEY_MARK},
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        while (cursor.moveToNext()) {
            // GET COLUMN INDICES + VALUES OF THOSE COLUMNS
            int id = cursor.getInt(cursor.getColumnIndex(LessonsDataBase.KEY_ID));
            String subject = cursor.getString(cursor.getColumnIndex(LessonsDataBase.KEY_SUBJECT));
            if (subject == name) {
                database.deleteItem(id);
            }
        }
        arrayAdapter = new ArrayAdapter<String>(this,   R.layout.my_style, subjects);
        listView.setAdapter(arrayAdapter);
        cursor.close();
    }
    private void readFromDateBase() {
        subjects = new ArrayList<String>();
        Cursor cursor = database.sqLiteDatabase.query(LessonsDataBase.TABLE_NAME, new String[] {
                LessonsDataBase.KEY_ID, LessonsDataBase.KEY_SUBJECT, LessonsDataBase.KEY_PART, LessonsDataBase.KEY_MARK},
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        while (cursor.moveToNext()) {
            // GET COLUMN INDICES + VALUES OF THOSE COLUMNS
            int id = cursor.getInt(cursor.getColumnIndex(LessonsDataBase.KEY_ID));
            String subject = cursor.getString(cursor.getColumnIndex(LessonsDataBase.KEY_SUBJECT));
            String part = cursor.getString(cursor.getColumnIndex(LessonsDataBase.KEY_PART));
            String mark = cursor.getString(cursor.getColumnIndex(LessonsDataBase.KEY_MARK));
            if (subjects.contains(subject) == false && part.equals("")) {
                subjects.add(subject + ". " + mark);
            }
        }
        arrayAdapter = new ArrayAdapter<String>(this,   R.layout.my_style, subjects);
        listView.setAdapter(arrayAdapter);
        cursor.close();
    }
}
