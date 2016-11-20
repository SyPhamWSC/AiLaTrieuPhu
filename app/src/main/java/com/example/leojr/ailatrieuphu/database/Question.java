package com.example.leojr.ailatrieuphu.database;

/**
 * Created by Leo Jr on 12/10/2016.
 */

public class Question {
    private String content;
    private String caseA;
    private String caseB;
    private String caseC;
    private String caseD;
    private int trueCase;
    private int id;
    private String level;


    Question(String content, String caseA, String caseB, String caseC, String caseD, int trueCase, String level){
        this.content = content;
        this.caseA = caseA;
        this.caseB = caseB;
        this.caseC = caseC;
        this.caseD = caseD;
        this.level = level;
        this.trueCase = trueCase;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCaseA() {
        return caseA;
    }

    public String getCaseB() {
        return caseB;
    }

    public String getCaseC() {
        return caseC;
    }

    public String getCaseD() {
        return caseD;
    }

    public int getTrueCase() {
        return trueCase;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
