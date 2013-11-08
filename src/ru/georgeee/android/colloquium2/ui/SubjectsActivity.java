package ru.georgeee.android.colloquium2.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import ru.georgeee.android.colloquium2.R;
import ru.georgeee.android.colloquium2.base.SFBaseActivity;
import ru.georgeee.android.colloquium2.db.MarkTable;
import ru.georgeee.android.colloquium2.db.SubjectTable;
import ru.georgeee.android.colloquium2.handlers.SFBaseCommand;
import ru.georgeee.android.colloquium2.handlers.impl.LoadMarksCommand;
import ru.georgeee.android.colloquium2.model.Mark;
import ru.georgeee.android.colloquium2.model.Subject;

import java.io.IOException;
import java.util.ArrayList;

public class SubjectsActivity extends SFBaseActivity {
    ListView subjectList;
    SubjectListAdapter subjectListAdapter;
    Button addSubjectButton;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subjects);
        subjectList = (ListView) findViewById(R.id.subjectList);
        subjectListAdapter = new SubjectListAdapter(this);
        subjectList.setAdapter(subjectListAdapter);
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        SubjectTable subjectTable = SubjectTable.getInstance(this);
        MarkTable markTable = MarkTable.getInstance(this);
        try {
            subjectListAdapter.addAll(subjectTable.loadSubjects());
        } catch (IOException e) {
            Log.e(SubjectsActivity.class.getCanonicalName(), e.toString());
        } catch (ClassNotFoundException e) {
            Log.e(SubjectsActivity.class.getCanonicalName(), e.toString());
        }
        subjectListAdapter.notifyDataSetInvalidated();
        registerForContextMenu(subjectList);
        subjectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Subject subject = subjectListAdapter.getItem(position);
                ProgressDialogFragment progress = new ProgressDialogFragment();
                progress.show(getSupportFragmentManager(), PROGRESS_DIALOG);
                requestId = getServiceHelper().loadMarks(subject);
            }
        });
        addSubjectButton = (Button) findViewById(R.id.addSubjectBtn);
        addSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Subject subject = new Subject();
                Intent intent = new Intent(v.getContext(), EditSubjectActivity.class);
                intent.putExtra("subject", subject);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onServiceCallback(int requestId, Intent requestIntent, int resultCode, Bundle resultData) {
        super.onServiceCallback(requestId, requestIntent, resultCode, resultData);

        if (getServiceHelper().check(requestIntent, LoadMarksCommand.class)) {
            if (resultCode == LoadMarksCommand.RESPONSE_SUCCESS) {
                dismissProgressDialog();
                Subject subject = (Subject) resultData.get("subject");
                ArrayList<Mark>  marks = (ArrayList<Mark>) resultData.get("marks");
                Intent intent = new Intent(this, MarksActivity.class);
                intent.putExtra("subject", subject);
                intent.putExtra("marks", marks);
                startActivity(intent);
            } else if (resultCode == LoadMarksCommand.RESPONSE_PROGRESS) {
                updateProgressDialog(resultData.getInt(SFBaseCommand.EXTRA_PROGRESS, -1));
            } else {
                dismissProgressDialog();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getExtras().containsKey("subject")){
            Subject subject = (Subject) data.getSerializableExtra("subject");
            subjectListAdapter.remove(subject);
            subjectListAdapter.add(subject);
            subjectListAdapter.notifyDataSetInvalidated();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.subjectList) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(subjectListAdapter.getItem(info.position).getName());
            menu.add(0, MENU_DELETE_ITEM, 0, getString(R.string.deleteBtn));
            menu.add(1, MENU_EDIT_ITEM, 1, getString(R.string.editBtn));
        }
    }

    public static final int MENU_DELETE_ITEM = 1;
    public static final int MENU_EDIT_ITEM = 1;

    @SuppressWarnings("deprecation")
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == MENU_DELETE_ITEM) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Subject subject = subjectListAdapter.getItem(info.position);
            subjectListAdapter.notifyDataSetInvalidated();
            subjectListAdapter.remove(subject);
            MarkTable.getInstance(this).deleteAllFromSubject(subject);
            SubjectTable.getInstance(this).delete(subject);
            return true;
        }else if(item.getItemId() == MENU_EDIT_ITEM){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Subject subject = subjectListAdapter.getItem(info.position);

            Intent intent = new Intent(this, EditSubjectActivity.class);
            intent.putExtra("subject", subject);
            startActivityForResult(intent, 1);

            return true;
        }
        return super.onContextItemSelected(item);
    }

    protected class SubjectListAdapter extends ArrayAdapter<Subject> {


        public SubjectListAdapter(Context context) {
            super(context, R.layout.subject_list_item);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflator = getLayoutInflater();
            View rowView = inflator.inflate(R.layout.subject_list_item, null, true);
            TextView subjectName = (TextView) rowView.findViewById(R.id.subjectName);
            TextView subjectMark = (TextView) rowView.findViewById(R.id.subjectMark);
            final Subject subject = getItem(position);
            subjectName.setText(subject.getName());
            subjectMark.setText(subject.getMark());
            return rowView;
        }
    }
}
