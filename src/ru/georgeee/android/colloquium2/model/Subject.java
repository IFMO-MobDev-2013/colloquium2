package ru.georgeee.android.colloquium2.model;

import ru.georgeee.android.colloquium2.db.MarkTable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: georgeee
 * Date: 12.10.13
 * Time: 4:11
 * To change this template use File | Settings | File Templates.
 */
public class Subject implements Serializable{
    protected long subjectId;
    protected String name;

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subject)) return false;

        Subject subject = (Subject) o;

        if (subjectId != subject.subjectId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (subjectId ^ (subjectId >>> 32));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMark() {
        return MarkTable.getInstance().getSumBySubjectId(subjectId);
    }

}
