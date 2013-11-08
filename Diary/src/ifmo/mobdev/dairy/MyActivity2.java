package ifmo.mobdev.dairy;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

public class MyActivity2 extends Activity {
    private RSSDBAdapter mDbHelper;
    private ListView lv;
    TextView sub_name;
    TextView sub_points;
    String name;
    String points;
    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;

    private void addTasksView() {
        Cursor tasksCursor = mDbHelper.fetchAllTasksBySub(mDbHelper.getSubIdByName(name));
        startManagingCursor(tasksCursor);

        String[] from = new String[]{RSSDBAdapter.KEY_NAME, RSSDBAdapter.KEY_TASK_POINTS};

        int[] to = new int[]{R.id.tw21, R.id.tw22};

        SimpleCursorAdapter tasksAdapter = new SimpleCursorAdapter(this, R.layout.list_item_w2, tasksCursor, from, to);
        lv.setAdapter(tasksAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.w2);

        lv = (ListView) findViewById(R.id.list2);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        name = extras.getString("sub");
        points = extras.getString("points");
        sub_name = (TextView) findViewById(R.id.tvsub);
        sub_points = (TextView) findViewById(R.id.tvpoints);
        sub_name.setText(name);
        sub_points.setText(points);

        mDbHelper = new RSSDBAdapter(this);
        mDbHelper.open();

        addTasksView();

        registerForContextMenu(lv);
    }

    public void createTask() {
        Intent i = new Intent(this, TaskEdit.class);
        i.putExtra("name", name);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.task_menu_insert);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case INSERT_ID:
                createTask();
                return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.task_menu_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                mDbHelper.deleteTask(info.id);
                addTasksView();
                return true;
        }
        return super.onContextItemSelected(item);
    }
}
