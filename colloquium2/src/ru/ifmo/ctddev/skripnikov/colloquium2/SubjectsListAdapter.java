package ru.ifmo.ctddev.skripnikov.colloquium2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SubjectsListAdapter extends ArrayAdapter<Subject> {
    private final Context context;
    private final Subject[] subjects;

    public SubjectsListAdapter(Context context, Subject[] subjects) {
        super(context, R.layout.subjects_list_item, subjects);
        this.context = context;
        this.subjects = subjects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View subjectListItem;
        if (convertView == null) {
            subjectListItem = ((LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.subjects_list_item, parent, false);
        } else {
            subjectListItem = convertView;
        }
        ((TextView) subjectListItem.findViewById(R.id.subjects_list_item_name))
                .setText(subjects[position].name);
        Integer d = subjects[position].rating;
        ((TextView) subjectListItem.findViewById(R.id.subjects_list_item_rating))
                .setText(d.toString());
        return subjectListItem;
    }
}
