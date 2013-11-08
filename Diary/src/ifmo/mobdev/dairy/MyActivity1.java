package ifmo.mobdev.dairy;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MyActivity1 extends Activity {

    private RSSDBAdapter mDbHelper;
    public static String MATH = "Математика";
    ListView lv;
    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;

    private void addSubsView() {
        Cursor feedsCursor = mDbHelper.fetchAllSubs();
        startManagingCursor(feedsCursor);

        String[] from = new String[]{RSSDBAdapter.KEY_NAME, RSSDBAdapter.KEY_POINTS};

        int[] to = new int[]{R.id.tw1, R.id.tw2};

        SimpleCursorAdapter feedsAdapter = new SimpleCursorAdapter(this, R.layout.list_item_w1, feedsCursor, from, to);
        lv.setAdapter(feedsAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.w1);

        lv = (ListView) findViewById(R.id.listViewW1);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String lstsub = ((TextView) view.findViewById(R.id.tw1)).getText().toString();
                String lstpoints = ((TextView) view.findViewById(R.id.tw2)).getText().toString();
                Intent intent = new Intent(MyActivity1.this, MyActivity2.class);
                intent.putExtra("points", lstpoints);
                intent.putExtra("sub", lstsub);
                startActivity(intent);
            }
        });



        mDbHelper = new RSSDBAdapter(this);
        mDbHelper.open();
        //mDbHelper.drop();

        if (mDbHelper.getSubIdByName(MATH) == -1) {
            mDbHelper.createSub(MATH);
            mDbHelper.createTask(mDbHelper.getSubIdByName(MATH), "ДЗ №1", "0");
        }

        addSubsView();
        registerForContextMenu(lv);
    }

    public void createSub() {
        Intent i = new Intent(this, SubEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.menu_insert);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case INSERT_ID:
                createSub();
                return true;
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
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                mDbHelper.deleteSub(info.id);
                addSubsView();
                return true;
        }
        return super.onContextItemSelected(item);
    }
}

