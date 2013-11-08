package ru.ifmo.rain.loboda.colloq2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MarksActivity extends Activity {
    private SQLRequester requester;
    private int id;
    public static final int CODE = 100;
    public static final int UPDATE_CODE = 101;
    private final int DELETE_ID = 0;
    private final int MODIFY_ID = 1;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marks_layout);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");
        requester = new SQLRequester(this);
        requester.open();
        fillData();
    }

    private void fillData(){
        ListView listView = (ListView)findViewById(R.id.marks_list_view);
        Cursor cursor = requester.fetchMarksBySubjectId(id);
        startManagingCursor(cursor);
        ((TextView)findViewById(R.id.subject_name)).setText(requester.getNameSubjectById(id));
        listView.setAdapter(new SimpleCursorAdapter(listView.getContext(), R.layout.row_in_main, cursor, new String[]{SQLRequester.MARK_NAME, SQLRequester.MARK}, new int[]{R.id.textView, R.id.textView1}));
        registerForContextMenu(listView);
        findViewById(R.id.add_mark_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddMarkActivity.class);
                startActivityForResult(intent, CODE);
            }
        });

        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(0, DELETE_ID, 0, "Удалить");
                contextMenu.add(0, MODIFY_ID, 0, "Изменить");
            }
        });
    }

    @Override
    public boolean onMenuItemSelected (int featureId, MenuItem item){
        int id = (int)((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).id;
        switch (item.getItemId()){
            case DELETE_ID:
                requester.deleteMarkById(id);
                break;
            case MODIFY_ID:
                Intent intent = new Intent(getBaseContext(), AddMarkActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("name", requester.getNameByMarkId(id));
                intent.putExtra("mark", requester.getMarkByMarkId(id));
                startActivityForResult(intent, UPDATE_CODE);
                break;
        }
        fillData();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Bundle res = data.getExtras();
        if(requestCode == CODE && data != null){
            requester.insertMark(id, res.getInt("mark"), res.getString("name"));
        }
        if(requestCode == UPDATE_CODE && data != null){
            requester.updateMarkById(id, res.getString("name"), res.getInt("mark"));
        }
        fillData();
    }

    public void onDestroy(){
        requester.close();
        super.onDestroy();
    }
}