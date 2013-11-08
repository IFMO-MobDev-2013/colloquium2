package ru.zulyaev.ifmo.colloquium2;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
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

/**
 * author: zulyaev
 */
public class ScoresActivity extends Activity {
    public static final String SUBJECT_ID_INDEX = "subject";
    public static final String DIALOG_TAG = "Dialog";

    private SimpleCursorAdapter adapter;
    private DbOpenHelper helper;
    private ScoresTable table;
    private String subjectTitle;
    private long id;

    private final ScoreCreateDialog.ScoreCreateDialogListener scoreCreateDialogListener = new ScoreCreateDialog.ScoreCreateDialogListener() {
        @Override
        public void onCreate(String title, int score) {
            table.addScore(id, title, score);
            refresh();
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView view = new ListView(this);

        id = getIntent().getLongExtra(SUBJECT_ID_INDEX, -1);
        if (id == -1) {
            finish();
        }

        helper = new DbOpenHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        table = new ScoresTable(db);
        table.selectAllById(id);
        SubjectsTable subjectsTable = new SubjectsTable(db);
        subjectTitle = subjectsTable.getSubjectTitle(id);

        adapter = new SimpleCursorAdapter(
                this,
                R.layout.list_item,
                table.selectAllById(id),
                new String[]{ScoresTable.COLUMN_TITLE, ScoresTable.COLUMN_SCORE},
                new int[]{R.id.title, R.id.score},
                0
        );
        view.setAdapter(adapter);

        view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
                new ScoreModifyDialog(new ScoreModifyDialog.ScoreModifyDialogListener() {
                    @Override
                    public void onSave(String title, int score) {
                        table.modifyScore(id, title, score);
                        refresh();
                    }
                    @Override
                    public void onDelete() {
                        table.deleteScore(id);
                        refresh();
                    }
                }).show(getFragmentManager(), DIALOG_TAG);
                return true;
            }
        });

        setContentView(view);
        updateSum();
    }

    private void refresh() {
        adapter.changeCursor(table.selectAllById(id));
        updateSum();
    }

    private void updateSum() {
        setTitle(subjectTitle + ": " + table.getSumScore(id));
    }

    @Override
    protected void onDestroy() {
        helper.close();
        super.onDestroy();
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
                new ScoreCreateDialog(scoreCreateDialogListener).show(getFragmentManager(), DIALOG_TAG);
                return true;
        }
        return false;
    }
}