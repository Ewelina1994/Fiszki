package com.example.fiszki.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {
    int id;
    private String question;

    public Question() {
    }

    public Question(Parcel in) {
        question=in.readString();
    }

    public Question(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question((in));
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}
