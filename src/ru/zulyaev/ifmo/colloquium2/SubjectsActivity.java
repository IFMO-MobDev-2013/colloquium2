package ru.zulyaev.ifmo.colloquium2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import ru.zulyaev.ifmo.colloquium2.db.DbOpenHelper;
import ru.zulyaev.ifmo.colloquium2.db.ScoresTable;
import ru.zulyaev.ifmo.colloquium2.db.SubjectsTable;

public class SubjectsActivity extends Activity {
    public static final String DIALOG_TAG = "Dialog";
    private SimpleCursorAdapter adapter;
    private DbOpenHelper helper;
    private SubjectsTable table;
    private final SubjectCreateDialog.SubjectCreateDialogListener subjectCreateDialogListener =
            new SubjectCreateDialog.SubjectCreateDialogListener() {
        @Override
        public void onCreate(String name) {
            table.addSubject(name);
            refresh();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView view = new ListView(this);
        helper = new DbOpenHelper(this);
        table = new SubjectsTable(helper.getWritableDatabase());
        adapter = new SimpleCursorAdapter(this,
                R.layout.list_item,
                table.selectAllWithScore(),
                new String[] {SubjectsTable.COLUMN_TITLE, ScoresTable.COLUMN_SCORE},
                new int[] {R.id.title, R.id.score},
                0
        );
        view.setAdapter(adapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SubjectsActivity.this, ScoresActivity.class);
                intent.putExtra(ScoresActivity.SUBJECT_ID_INDEX, id);
                startActivity(intent);
            }
        });
        view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
                new SubjectModifyDialog(new SubjectModifyDialog.SubjectModifyDialogListener() {
                    @Override
                    public void onSave(String name) {
                        table.modifySubject(id, name);
                        refresh();
                    }

                    @Override
                    public void onDelete() {
                        table.deleteSubject(id);
                        refresh();
                    }
                }).show(getFragmentManager(), DIALOG_TAG);
                return true;
            }
        });
        setContentView(view);
    }

    private void refresh() {
        adapter.changeCursor(table.selectAllWithScore());
    }

    @Override
    protected void onDestroy() {
        helper.close();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                new SubjectCreateDialog(subjectCreateDialogListener).show(getFragmentManager(), DIALOG_TAG);
                return true;
        }
        return false;
    }
}
