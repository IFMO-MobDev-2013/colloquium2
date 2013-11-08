package com.example.HometaskScheduler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

public class SubjectActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
   /* @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subjects);
    }    */
    private ArrayList<Subject> values = new ArrayList<>();
    private ScheduleDAO dao = new ScheduleDAO(this);

    private Context getContext() {
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subjects);
        final ListView marks = (ListView) findViewById(R.id.subjects);
        values = dao.getAllSubjects();
        final SubjectsArrayAdapter adapter = new SubjectsArrayAdapter(this, android.R.layout.simple_list_item_1, values);
        Button addNew = new Button(this);
        addNew.setText("Add new subject");
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LayoutInflater inflater = getLayoutInflater();
                final View layout = inflater.inflate(R.layout.new_subject,
                        (ViewGroup) findViewById(R.id.addNewSubject));
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("New subject");
                alert.setView(layout);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String subjectDescription = ((EditText) layout.findViewById(R.id.editSubject)).getText().toString();
                        Subject subject = new Subject(subjectDescription);
                        dao.addSubject(subject);
                        values.add(subject);
                        adapter.notifyDataSetChanged();
                    }
                });

                alert.setNegativeButton("Cancel", null);
                alert.show();
            }
        });
        marks.addHeaderView(addNew);
        marks.setAdapter(adapter);
        marks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Subject subject = values.get(i-1);
                Intent intent = new Intent(getContext(), MarksActivity.class);
                intent.putExtra("subjectID", subject.dbID);
                startActivity(intent);
            }
        });
        marks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long l) {
                final Subject subject = values.get(position-1);
                final LayoutInflater inflater = getLayoutInflater();
                final View layout = inflater.inflate(R.layout.new_subject,
                        (ViewGroup) findViewById(R.id.addNewSubject));
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Edit subject");
                ((EditText) layout.findViewById(R.id.editSubject)).setText(subject.description);
                alert.setView(layout);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String subjectDescription = ((EditText) layout.findViewById(R.id.editSubject)).getText().toString();
                        subject.description = subjectDescription;
                        dao.editSubject(subject);
                        adapter.notifyDataSetChanged();
                    }
                });
                alert.setNeutralButton("Remove", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dao.deleteSubject(values.get(position-1).dbID);
                        Log.e("ololo", String.valueOf(values.get(position-1).dbID));
                        values.remove(position-1);
                        adapter.notifyDataSetChanged();
                    }
                });

                alert.setNegativeButton("Cancel", null);
                alert.show();
                return true;
            }
        });
    }

    public class SubjectsArrayAdapter extends ArrayAdapter<Subject> {

        public SubjectsArrayAdapter(Context context, int resource, List<Subject> objects) {
            super(context, resource, objects);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            TextView descr = new TextView(getContext());
            descr.setText(this.getItem(pos).description + "  " + this.getItem(pos).currentPoints);
            descr.setTextSize(20);
            return descr;
        }
    }


}
