package ru.ifmo.ctddev.skripnikov.colloquium2;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class EditSubjectDialog extends DialogFragment implements View.OnClickListener {
    Subject subject;
    Listener listener;
    Context context;
    EditText etName;

    EditSubjectDialog(Context context, Subject subject, Listener listener) {
        this.context = context;
        this.subject = subject;
        this.listener = listener;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(R.string.edit_subject);
        View v = inflater.inflate(R.layout.edit_subject_dialog, null);
        v.findViewById(R.id.edit_subject_dialog_edit).setOnClickListener(this);
        v.findViewById(R.id.edit_subject_dialog_delete).setOnClickListener(this);
        v.findViewById(R.id.edit_subject_dialog_cancel).setOnClickListener(this);
        etName = (EditText) v.findViewById(R.id.edit_subject_dialog_name);
        etName.setText(subject.name);
        return v;
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.edit_subject_dialog_edit:
                String s = etName.getText().toString();
                if (s.length() > 0) {
                    subject.name = s;
                    DBStorage dbStorage = new DBStorage(context);
                    dbStorage.changeSubject(subject);
                    dbStorage.destroy();
                    listener.onDialogDismissed();
                    dismiss();
                }
                break;
            case R.id.edit_subject_dialog_delete:
                DBStorage dbStorage = new DBStorage(context);
                dbStorage.deleteSubject(subject.id);
                dbStorage.destroy();
                listener.onDialogDismissed();
                dismiss();
                break;
            case R.id.edit_subject_dialog_cancel:
                dismiss();
                break;
        }
    }
}
