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

public class MainActivity extends Activity {
    public static final int CODE = 200;
    public static final int UPDATE_CODE = 201;
    private SQLRequester requester;
    private final int DELETE_ID = 0;
    private final int MODIFY_ID = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        requester = new SQLRequester(this);
        requester.open();
        fillData();

        findViewById(R.id.add_subject_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddSubject.class);
                startActivityForResult(intent, CODE);
            }
        });
    }

    private void fillData(){
        ListView listView = (ListView)findViewById(R.id.listView);
        Cursor listOfSubjects = requester.fetchAllSubjects();
        startManagingCursor(listOfSubjects);
        listView.setAdapter(new SimpleCursorAdapter(this, R.layout.row_in_main, listOfSubjects, new String[]{"name"}, new int[]{R.id.textView}));
        registerForContextMenu(listView);

        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(0, DELETE_ID, 0, "Удалить");
                contextMenu.add(0, MODIFY_ID, 0, "Изменить");
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(), MarksActivity.class);
                intent.putExtra("id", (int)l);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onMenuItemSelected (int featureId, MenuItem item){
        int id = (int)((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).id;
        switch (item.getItemId()){
            case DELETE_ID:
                requester.deleteSubjectById(id);
                fillData();
                break;
            case MODIFY_ID:
                Intent intent = new Intent(getBaseContext(), AddSubject.class);
                intent.putExtra("id", id);
                intent.putExtra("name", requester.getNameSubjectById(id));
                startActivityForResult(intent, UPDATE_CODE);
                break;
        }
        return true;
    }

    @Override
    public void onDestroy(){
        requester.close();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == CODE && data != null){
            requester.insertSubject(data.getExtras().getString("name"));
        }
        if(requestCode == UPDATE_CODE && data != null){
            Bundle bundle = data.getExtras();
            requester.updateNameSubject(bundle.getInt("id"), bundle.getString("name"));
        }
        fillData();
    }
}
