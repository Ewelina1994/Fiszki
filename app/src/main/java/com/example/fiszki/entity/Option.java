package com.example.fiszki.entity;

import android.os.Parcel;
import android.os.Parcelable;

//będzie można potem usunąc tą percable
public class Option implements Parcelable {
    //metoda do generowania id
    private static long idCounter=0;

    protected Option(Parcel in) {
        id = in.readLong();
        question_id = in.readLong();
        name = in.readString();
        is_right = in.readInt();
        language = in.readString();
    }

    public static final Creator<Option> CREATOR = new Creator<Option>() {
        @Override
        public Option createFromParcel(Parcel in) {
            return new Option(in);
        }

        @Override
        public Option[] newArray(int size) {
            return new Option[size];
        }
    };

    public static synchronized Long createID()
    {
        return (idCounter++);
    }

    long id;
    long question_id;
    String name;
    int is_right;
    String language;

    public Option(long question_id, String option, int is_right, String language) {
        this.id=createID();
        this.question_id = question_id;
        this.name = option;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(question_id);
        dest.writeString(name);
        dest.writeInt(is_right);
        dest.writeString(language);
    }
}
