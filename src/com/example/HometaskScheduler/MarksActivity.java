package com.example.HometaskScheduler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Xottab
 * Date: 08.11.13
 * Time: 13:24
 * To change this template use File | Settings | File Templates.
 */
public class MarksActivity extends Activity {
    private ArrayList<Mark> values = new ArrayList<>();
    private ScheduleDAO dao = new ScheduleDAO(this);
    private Integer subjectID;

    private Context getContext() {
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marks);
        subjectID = getIntent().getIntExtra("subjectID", 0);
        final ListView marks = (ListView) findViewById(R.id.marks);
        values = dao.getAllMarks(subjectID);
        final MarksArrayAdapter adapter = new MarksArrayAdapter(this, android.R.layout.simple_list_item_1, values);
        Button addNew = new Button(this);
        addNew.setText("Add new mark");
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LayoutInflater inflater = getLayoutInflater();
                final View layout = inflater.inflate(R.layout.new_mark,
                        (ViewGroup) findViewById(R.id.addNewMark));
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("New mark");
                alert.setView(layout);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String markDescription = ((EditText) layout.findViewById(R.id.markDescription)).getText().toString();
                        Integer points;
                        try {
                            points = Integer.valueOf(((EditText) layout.findViewById(R.id.markPoints)).getText().toString());
                        } catch (NumberFormatException e) {
                            points = 0;
                        }
                        Mark mark = new Mark(subjectID, markDescription, points);
                        dao.addMark(mark);
                        values.add(mark);
                        adapter.notifyDataSetChanged();
                    }
                });

                alert.setNegativeButton("Cancel", null);
                alert.show();
            }
        });
        marks.addHeaderView(addNew);
        marks.setAdapter(adapter);

        marks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long l) {
                final Mark mark = values.get(position-1);
                final LayoutInflater inflater = getLayoutInflater();
                final View layout = inflater.inflate(R.layout.new_mark,
                        (ViewGroup) findViewById(R.id.addNewMark));
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Edit mark");
                ((EditText) layout.findViewById(R.id.markDescription)).setText(mark.description);
                ((EditText) layout.findViewById(R.id.markPoints)).setText(Integer.toString(mark.points));
                alert.setView(layout);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String markDescription = ((EditText) layout.findViewById(R.id.markDescription)).getText().toString();
                        Integer points;
                        try {
                            points = Integer.valueOf(((EditText) layout.findViewById(R.id.markPoints)).getText().toString());
                        } catch (NumberFormatException e) {
                            points = 0;
                        }
                        mark.description = markDescription;
                        mark.points = points;
                        dao.editMark(mark);
                        adapter.notifyDataSetChanged();
                    }
                });
                alert.setNeutralButton("Remove", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dao.deleteMark(values.get(position-1).dbID);
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

    public class MarksArrayAdapter extends ArrayAdapter<Mark> {

        public MarksArrayAdapter(Context context, int resource, List<Mark> objects) {
            super(context, resource, objects);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            TextView descr = new TextView(getContext());
            descr.setText(this.getItem(pos).description + "  " + this.getItem(pos).points);
            descr.setTextSize(20);
            return descr;
        }
    }


}
