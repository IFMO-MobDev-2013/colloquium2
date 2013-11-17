package com.example.colloquium2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Vector;

public class SubjectsAdapter extends ArrayAdapter<Subject>
{
    private final Context context;
    public Vector<Subject> channels;
    private Program program;

    public SubjectsAdapter(Context context, Vector<Subject> subjects, Program program)
    {
        super(context, R.layout.subject, subjects);
        this.context = context;
        this.channels = subjects;
        this.program = program;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View entryView = inflater.inflate(R.layout.subject, parent, false);

        TextView subjectNameView = (TextView) entryView.findViewById(R.id.subjectName);
        TextView subjectPointsView = (TextView) entryView.findViewById(R.id.subjectPoints);

        final Subject subject = channels.get(index);

        subjectNameView.setText(subject.name);
        subjectPointsView.setText(subject.points+" points");

        entryView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(program, MarksActivity.class);
                intent.putExtra("ID_SUBJECT", subject.id_subject);
                program.startActivity(intent);
            }
        });

        entryView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v)
            {
                Intent intent = new Intent(program, MarkEditActivity.class);
                intent.putExtra("ID_SUBJECT", subject.id_subject);
                program.startActivity(intent);
                return true;
            }
        });

        return entryView;
    }
}
