package com.example.colloquium2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Vector;

class MarksAdapter extends ArrayAdapter<Mark>
{
    private final Context context;
    public Vector<Mark> marks;
    private MarksActivity program;

    public MarksAdapter(Context context, Vector<Mark> entries, MarksActivity program)
    {
        super(context, R.layout.mark, entries);
        this.context = context;
        this.marks = entries;
        this.program = program;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View markView = inflater.inflate(R.layout.mark, parent, false);

        TextView markTitleView = (TextView) markView.findViewById(R.id.markTitle);
        TextView markPointsView = (TextView) markView.findViewById(R.id.markPoints);

        final Mark mark = marks.get(index);

        markTitleView.setText(mark.title);
        markPointsView.setText(mark.points+" points");

        markView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(program, MarkEditActivity.class);
                intent.putExtra("ID_MARK", mark.id_mark);
                program.startActivity(intent);
                return true;
            }
        });

        return markView;
    }

}
