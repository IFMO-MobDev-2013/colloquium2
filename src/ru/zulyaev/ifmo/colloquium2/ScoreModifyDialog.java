package ru.zulyaev.ifmo.colloquium2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

/**
 * author: zulyaev
 */
public class ScoreModifyDialog extends DialogFragment {
    private Context context;
    private final ScoreModifyDialogListener listener;

    public ScoreModifyDialog(ScoreModifyDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(context).inflate(R.layout.score_dialog, null, false);
        final EditText title = (EditText) view.findViewById(R.id.title);
        final NumberPicker picker = (NumberPicker) view.findViewById(R.id.score);
        title.setSingleLine(true);
        picker.setMinValue(0);
        picker.setMaxValue(100);
        return new AlertDialog.Builder(context)
                .setView(view)
                .setTitle(R.string.modify_score)
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
                        listener.onSave(title.getText().toString(), picker.getValue());
                    }
                })
                .create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    interface ScoreModifyDialogListener {
        void onSave(String title, int score);
        void onDelete();
    }
}
