package ru.ifmo.ctddev.skripnikov.colloquium2;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class AddRatingDialog extends DialogFragment implements View.OnClickListener {
    Subject subject;
    Listener listener;
    Context context;
    EditText etDescription;
    EditText etMark;

    AddRatingDialog(Context context, Subject subject, Listener listener) {
        this.context = context;
        this.subject = subject;
        this.listener = listener;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(R.string.add_rating);
        View v = inflater.inflate(R.layout.add_rating_dialog, null);
        v.findViewById(R.id.add_rating_dialog_add).setOnClickListener(this);
        v.findViewById(R.id.add_rating_dialog_cancel).setOnClickListener(this);
        etDescription = (EditText) v.findViewById(R.id.add_rating_dialog_description);
        etMark = (EditText) v.findViewById(R.id.add_rating_dialog_mark);
        return v;
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.add_rating_dialog_add:
                String d = etDescription.getText().toString();
                String m = etMark.getText().toString();
                if (d.length() > 0) {
                    try {
                        int mark = Integer.parseInt(m);
                        subject.rating += mark;
                        DBStorage dbStorage = new DBStorage(context);
                        dbStorage.addRating(subject.id, mark, d);
                        dbStorage.changeSubject(subject);
                        dbStorage.destroy();
                        listener.onDialogDismissed();
                        dismiss();
                    } catch (NumberFormatException ignored) {
                    }
                }
                break;
            case R.id.add_rating_dialog_cancel:
                dismiss();
                break;
        }
    }
}
