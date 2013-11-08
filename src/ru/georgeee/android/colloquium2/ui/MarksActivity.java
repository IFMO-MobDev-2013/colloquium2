package ru.georgeee.android.colloquium2.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import ru.georgeee.android.colloquium2.R;
import ru.georgeee.android.colloquium2.base.SFBaseActivity;
import ru.georgeee.android.colloquium2.db.MarkTable;
import ru.georgeee.android.colloquium2.handlers.SFBaseCommand;
import ru.georgeee.android.colloquium2.handlers.impl.LoadMarksCommand;
import ru.georgeee.android.colloquium2.model.Mark;
import ru.georgeee.android.colloquium2.model.Subject;

import java.util.ArrayList;
import java.util.Collections;

public class MarksActivity extends SFBaseActivity {
    Subject subject;
    ListView markList;
    TextView feedTitle;
    EntryListAdapter markListAdapter;
    Button backBtn;
    Button addMarkBtn;
    TextView subjectMark;
    ArrayList<Mark> marks = new ArrayList<Mark>();

    @Override
    public void onServiceCallback(int requestId, Intent requestIntent, int resultCode, Bundle resultData) {
        super.onServiceCallback(requestId, requestIntent, resultCode, resultData);
        if (getServiceHelper().check(requestIntent, LoadMarksCommand.class)) {
            if (resultCode == LoadMarksCommand.RESPONSE_SUCCESS) {
                dismissProgressDialog();
                subject = (Subject) resultData.get("subject");
                ArrayList<Mark> entries = (ArrayList<Mark>) resultData.get("marks");
                reloadFeed(entries);
            } else if (resultCode == LoadMarksCommand.RESPONSE_PROGRESS) {
                updateProgressDialog(resultData.getInt(SFBaseCommand.EXTRA_PROGRESS, -1));
            } else {
                dismissProgressDialog();
            }
        }
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marks);
        markList = (ListView) findViewById(R.id.markList);
        feedTitle = (TextView) findViewById(R.id.subjectName);
        subjectMark = (TextView) findViewById(R.id.subjectMark);
        backBtn = (Button) findViewById(R.id.backBtn);
        addMarkBtn = (Button) findViewById(R.id.addMarkBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        markListAdapter = new EntryListAdapter(this);
        markList.setAdapter(markListAdapter);
        registerForContextMenu(markList);
        addMarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mark mark = new Mark();
                mark.setSubjectId(subject.getSubjectId());
                Intent intent = new Intent(view.getContext(), EditMarkActivity.class);
                intent.putExtra("subject", subject);
                intent.putExtra("mark", mark);
                startActivityForResult(intent, 1);
            }
        });
        Bundle extras = getIntent().getExtras();
        subject = (Subject) extras.get("subject");
        ArrayList<Mark> marks = (ArrayList<Mark>) extras.get("marks");
        subjectMark.setText(String.valueOf(subject.getMark()));
        reloadFeed(marks);
        onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null && data.getExtras().containsKey("mark")){
            Mark mark = (Mark) data.getSerializableExtra("mark");
            markListAdapter.remove(mark);
            markListAdapter.add(mark);
            markListAdapter.notifyDataSetInvalidated();
            subjectMark.setText(String.valueOf(subject.getMark()));
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.markList) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(markListAdapter.getItem(info.position).getName());
            menu.add(0, MENU_DELETE_ITEM, 0, getString(R.string.deleteBtn));
            menu.add(1, MENU_EDIT_ITEM, 1, getString(R.string.editBtn));
        }
    }

    public static final int MENU_DELETE_ITEM = 1;
    public static final int MENU_EDIT_ITEM = 2;

    @SuppressWarnings("deprecation")
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == MENU_DELETE_ITEM) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Mark mark = markListAdapter.getItem(info.position);
            markListAdapter.notifyDataSetInvalidated();
            markListAdapter.remove(mark);
            MarkTable.getInstance(this).delete(mark);
            subjectMark.setText(String.valueOf(subject.getMark()));
            return true;
        }else if(item.getItemId() == MENU_EDIT_ITEM){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Mark mark = markListAdapter.getItem(info.position);

            Intent intent = new Intent(this, EditMarkActivity.class);
            intent.putExtra("subject", subject);
            intent.putExtra("mark", mark);
            startActivityForResult(intent, 1);

            return true;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        onPause();
    }

    public void reloadFeed(ArrayList<Mark> _marks) {
        if (subject == null) {
            feedTitle.setText("Null");
            return;
        }
        feedTitle.setText(subject.getName());
        Collections.sort(_marks);
        marks = _marks;
        markListAdapter.clear();
        markListAdapter.addAll(marks);
        markListAdapter.notifyDataSetInvalidated();
    }

    protected class EntryListAdapter extends ArrayAdapter<Mark> {


        public EntryListAdapter(Context context) {
            super(context, R.layout.mark_row);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflator = getLayoutInflater();
            View rowView = inflator.inflate(R.layout.mark_row, null, true);
            TextView markName = (TextView) rowView.findViewById(R.id.markName);
            TextView markValue = (TextView) rowView.findViewById(R.id.markValue);
            final Mark mark = getItem(position);
            markName.setText(mark.getName());
            markValue.setText(String.valueOf(mark.getValue()));
            return rowView;
        }
    }
}
