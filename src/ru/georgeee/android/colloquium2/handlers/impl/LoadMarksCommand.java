package ru.georgeee.android.colloquium2.handlers.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ResultReceiver;
import ru.georgeee.android.colloquium2.db.MarkTable;
import ru.georgeee.android.colloquium2.handlers.SFBaseCommand;
import ru.georgeee.android.colloquium2.model.Mark;
import ru.georgeee.android.colloquium2.model.Subject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: georgeee
 * Date: 24.10.13
 * Time: 8:56
 * To change this template use File | Settings | File Templates.
 */
public class LoadMarksCommand extends SFBaseCommand {

    Subject subject;

    @Override
    protected void doExecute(Intent intent, Context context, ResultReceiver callback) {
        MarkTable table = MarkTable.getInstance(context);
        Bundle bundle = new Bundle();
        try{
            Mark[] marks = table.loadMarks(subject);
            ArrayList<Mark> _marks = new ArrayList<Mark>();
            Collections.addAll(_marks, marks);
            bundle.putSerializable("marks", _marks);
            bundle.putSerializable("subject", subject);
            notifySuccess(bundle);
        }catch (Exception ex){
            bundle.putSerializable("exceptionClass", ex.getClass());
            bundle.putString("exceptionMessage", ex.getMessage());
            notifyFailure(bundle);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(subject);
    }
    public static final Parcelable.Creator<LoadMarksCommand> CREATOR = new Parcelable.Creator<LoadMarksCommand>() {
        public LoadMarksCommand createFromParcel(Parcel in) {
            return new LoadMarksCommand(in);
        }

        public LoadMarksCommand[] newArray(int size) {
            return new LoadMarksCommand[size];
        }
    };

    private LoadMarksCommand(Parcel in) {
        subject = (Subject) in.readSerializable();
    }

    public LoadMarksCommand(Subject subject) {
        this.subject = subject;
    }

}
