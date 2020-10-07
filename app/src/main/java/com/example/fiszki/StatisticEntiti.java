package com.example.fiszki;

import android.os.Parcel;
import android.os.Parcelable;

public class StatisticEntiti {
    public static final String DIFFICULTY_EASY="Easy";
    public static final String DIFFICULTY_MEDIUM="Medium";
    public static final String DIFFICULTY_HARD="Hard";

    private int wrong;
    private int score;
    private String difficulty;
    private String data;

    public StatisticEntiti(){}

    public StatisticEntiti(Parcel in){
        wrong=in.readInt();
        score=in.readInt();
        difficulty=in.readString();
        data=in.readString();
    }

    public StatisticEntiti(int score, int wrong, String difficulty, String data) {
        this.wrong = wrong;
        this.score = score;
        this.difficulty = difficulty;
        this.data=data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getWrong() {
        return wrong;
    }

    public void setWrong(int wrong) {
        this.wrong = wrong;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(score);
        dest.writeInt(wrong);
        dest.writeString(difficulty);
        dest.writeString(data);
    }

    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
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
