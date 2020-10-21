package com.example.fiszki;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.fiszki.entity.Option;
import com.example.fiszki.entity.Question;

import java.util.List;

public class QuestionDTO implements Parcelable {
    Question question;
    List<Option> options;

    public QuestionDTO(Question question, List<Option> options) {
        this.question = question;
        this.options = options;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
