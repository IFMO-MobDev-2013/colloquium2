package ru.zulyaev.ifmo.colloquium2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

/**
 * author: zulyaev
 */
public class SubjectCreateDialog extends DialogFragment {
    private Context context;
    private final SubjectCreateDialogListener listener;

    public SubjectCreateDialog(SubjectCreateDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final EditText view = new EditText(context);
        view.setHint(R.string.subject_title_hint);
        view.setSingleLine(true);
        return new AlertDialog.Builder(context)
                .setView(view)
                .setTitle(R.string.new_subject)
                .setNegativeButton(R.string.cancel, NoopOnClickListener.INSTANCE)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onCreate(view.getText().toString());
                    }
                })
                .create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    interface SubjectCreateDialogListener {
        void onCreate(String name);
    }
}
