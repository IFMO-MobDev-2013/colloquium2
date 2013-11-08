package com.example.col2;

/**
 * Created with IntelliJ IDEA.
 * User: Александр
 * Date: 08.11.13
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */
public class SubjectNode {
    private String subjectName;
    private String score;

    public SubjectNode(String subjectName, String score) {
        this.subjectName = subjectName;
        this.score = score;
    }

    public String getSubject() {
        return subjectName;
    }

    public String getScore() {
        return score;
    }
}
