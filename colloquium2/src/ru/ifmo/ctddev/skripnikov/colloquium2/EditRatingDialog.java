package ru.ifmo.ctddev.skripnikov.colloquium2;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class EditRatingDialog extends DialogFragment implements View.OnClickListener {
    Subject subject;
    Rating rating;
    Listener listener;
    Context context;
    EditText etDescription;
    EditText etMark;

    EditRatingDialog(Context context, Subject subject, Rating rating, Listener listener) {
        this.context = context;
        this.subject = subject;
        this.rating = rating;
        this.listener = listener;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(R.string.edit_rating);
        View v = inflater.inflate(R.layout.edit_rating_dialog, null);
        v.findViewById(R.id.edit_rating_dialog_edit).setOnClickListener(this);
        v.findViewById(R.id.edit_rating_dialog_delete).setOnClickListener(this);
        v.findViewById(R.id.edit_rating_dialog_cancel).setOnClickListener(this);
        etDescription = (EditText) v.findViewById(R.id.edit_rating_dialog_description);
        etMark = (EditText) v.findViewById(R.id.edit_rating_dialog_mark);
        etDescription.setText(rating.description);
        etMark.setText((new Integer(rating.value)).toString());
        return v;
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.edit_rating_dialog_edit:
                String d = etDescription.getText().toString();
                String m = etMark.getText().toString();
                if (d.length() > 0) {
                    try {
                        int mark = Integer.parseInt(m);
                        subject.rating -= rating.value;
                        rating.description = d;
                        rating.value = mark;
                        subject.rating += rating.value;
                        DBStorage dbStorage = new DBStorage(context);
                        dbStorage.changeRating(rating);
                        dbStorage.changeSubject(subject);
                        dbStorage.destroy();
                        listener.onDialogDismissed();
                        dismiss();
                    } catch (NumberFormatException ignored) {
                    }
                }
                break;
            case R.id.edit_rating_dialog_delete:
                subject.rating -= rating.value;
                DBStorage dbStorage = new DBStorage(context);
                dbStorage.deleteRating(rating.id);
                dbStorage.changeSubject(subject);
                dbStorage.destroy();
                listener.onDialogDismissed();
                dismiss();
                break;
            case R.id.edit_rating_dialog_cancel:
                dismiss();
                break;
        }
    }
}
