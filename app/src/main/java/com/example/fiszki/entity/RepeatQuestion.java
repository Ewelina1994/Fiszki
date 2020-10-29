package com.example.fiszki.entity;

public class RepeatQuestion {
    private static long idCounter=0;
    public static synchronized Long createID()
    {
        return (idCounter++);
    }

    private long id;
    private long question;

    public RepeatQuestion(long question) {
        this.id=createID();
        this.question = question;
    }

    public RepeatQuestion() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getQuestion() {
        return question;
    }

    public void setQuestion(long question) {
        this.question = question;
    }
}
