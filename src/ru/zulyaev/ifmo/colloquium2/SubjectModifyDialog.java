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
public class SubjectModifyDialog extends DialogFragment {
    private Context context;
    private final SubjectModifyDialogListener listener;

    public SubjectModifyDialog(SubjectModifyDialogListener listener) {
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
                .setNeutralButton(R.string.cancel, NoopOnClickListener.INSTANCE)
                .setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDelete();
                    }
                })
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onSave(view.getText().toString());
                    }
                })
                .create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    interface SubjectModifyDialogListener {
        void onSave(String name);
        void onDelete();
    }
}
