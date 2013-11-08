package ru.georgeee.android.colloquium2.handlers.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.ResultReceiver;
import ru.georgeee.android.colloquium2.db.SubjectTable;
import ru.georgeee.android.colloquium2.handlers.SFBaseCommand;
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
public class LoadSubjectsCommand extends SFBaseCommand {

    @Override
    protected void doExecute(Intent intent, Context context, ResultReceiver callback) {
        SubjectTable table = SubjectTable.getInstance(context);
        Bundle bundle = new Bundle();
        try{
            Subject[] subjects = table.loadSubjects();
            ArrayList<Subject> _subjects = new ArrayList<Subject>();
            Collections.addAll(_subjects, subjects);
            bundle.putSerializable("subjects", _subjects);
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
    }
    public static final Creator<LoadSubjectsCommand> CREATOR = new Creator<LoadSubjectsCommand>() {
        public LoadSubjectsCommand createFromParcel(Parcel in) {
            return new LoadSubjectsCommand(in);
        }

        public LoadSubjectsCommand[] newArray(int size) {
            return new LoadSubjectsCommand[size];
        }
    };

    private LoadSubjectsCommand(Parcel in) {
    }

    public LoadSubjectsCommand() {
    }

}
