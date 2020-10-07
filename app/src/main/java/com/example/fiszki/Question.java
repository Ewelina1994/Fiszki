package com.example.fiszki;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {
    public static final String DIFFICULTY_EASY="Easy";
    public static final String DIFFICULTY_MEDIUM="Medium";
    public static final String DIFFICULTY_HARD="Hard";

    private String question;
    private String options1;
    private String options2;
    private String options3;
    private int answerNr;
    private String difficulty;

    public Question(){

    }

    public Question(Parcel in) {
        question=in.readString();
        options1=in.readString();
        options2=in.readString();
        options3=in.readString();
        answerNr=in.readInt();
        difficulty=in.readString();
    }

    public Question(String question, String options1, String options2, String options3, int answer, String difficulty) {
        this.question = question;
        this.options1 = options1;
        this.options2 = options2;
        this.options3 = options3;
        this.answerNr=answer;
        this.difficulty=difficulty;
    }

    public String getQuestion() {
        return question;
    }

    public String getOptions1() {
        return options1;
    }

    public String getOptions2() {
        return options2;
    }

    public String getOptions3() {
        return options3;
    }

    public int getAnswerNr() {
        return answerNr;
    }

    public void setAnswerNr(int answerNr) {
        this.answerNr = answerNr;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setOptions1(String options1) {
        this.options1 = options1;
    }

    public void setOptions2(String options2) {
        this.options2 = options2;
    }

    public void setOptions3(String options3) {
        this.options3 = options3;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public static String[] getAllDifficultyLevels(){
        return new String[]{
                DIFFICULTY_EASY,
                DIFFICULTY_MEDIUM,
                DIFFICULTY_HARD
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(options1);
        dest.writeString(options2);
        dest.writeString(options3);
        dest.writeInt(answerNr);
        dest.writeString(difficulty);
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
