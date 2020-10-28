package com.example.fiszki.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {
    //metoda do generowania id
    private static long idCounter=0;
    public static synchronized Long createID()
    {
        return (idCounter++);
    }

    long id;
    private String question;

    public Question() {
    }

    public Question(Parcel in) {
        question=in.readString();
    }

    public Question(String question) {
        this.question = question;
        this.id=createID();
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
