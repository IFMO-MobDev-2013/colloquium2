package ru.ifmo.ctddev.skripnikov.colloquium2;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class AddSubjectDialog extends DialogFragment implements View.OnClickListener {
    Listener listener;
    Context context;
    EditText etName;

    AddSubjectDialog(Context context, Listener listener) {
        this.context = context;
        this.listener = listener;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(R.string.add_subject);
        View v = inflater.inflate(R.layout.add_subject_dialog, null);
        v.findViewById(R.id.add_subject_dialog_add).setOnClickListener(this);
        v.findViewById(R.id.add_subject_dialog_cancel).setOnClickListener(this);
        etName = (EditText) v.findViewById(R.id.add_subject_dialog_name);
        return v;
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.add_subject_dialog_add:
                String s = etName.getText().toString();
                if (s.length() > 0) {
                    DBStorage dbStorage = new DBStorage(context);
                    dbStorage.addSubject(s);
                    dbStorage.destroy();
                    listener.onDialogDismissed();
                    dismiss();
                }
                break;
            case R.id.add_subject_dialog_cancel:
                dismiss();
                break;
        }
    }
}
