package com.example.fiszki.entity;

public class Option {
    long id;
    long question_id;
    String option;
    int is_right;
    String language;

    public Option(long question_id, String option, int is_right, String language) {
        this.question_id = question_id;
        this.option = option;
        this.is_right = is_right;
        this.language = language;
    }

    public Option() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(long question_id) {
        this.question_id = question_id;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public int getIs_right() {
        return is_right;
    }

    public void setIs_right(int is_right) {
        this.is_right = is_right;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
