package ru.georgeee.android.colloquium2.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: georgeee
 * Date: 12.10.13
 * Time: 4:39
 * To change this template use File | Settings | File Templates.
 */
public class Mark implements Serializable, Comparable<Mark> {
    protected long markId;
    protected long subjectId;
    protected String name;
    protected int value;

    public long getMarkId() {
        return markId;
    }

    public void setMarkId(long markId) {
        this.markId = markId;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }


    @Override
    public int compareTo(Mark mark) {
        return name.compareTo(mark.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mark)) return false;

        Mark mark = (Mark) o;

        if (markId != mark.markId) return false;
        if (subjectId != mark.subjectId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (markId ^ (markId >>> 32));
        result = 31 * result + (int) (subjectId ^ (subjectId >>> 32));
        return result;
    }
}
