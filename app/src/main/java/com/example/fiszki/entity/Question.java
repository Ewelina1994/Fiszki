package com.example.fiszki.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {
    //metoda do generowania id
//    private static long idCounter=1;
//    public static synchronized Long createID()
//    {
//        return (idCounter++);
//    }

    long id;
    private String name;
    private byte[]  name_image;

    public Question() {
    }

    public Question(Parcel in) {
       // id=in.readLong();
        name=in.readString();
        name_image = new byte[in.readInt()];
        in.readByteArray(name_image);
    }

    public Question(String question) {
        this.name = question;
       // this.id=createID();
    }

    public Question(String question, byte[] image) {
        this.name_image=image;
        this.name = question;
       // this.id=createID();
    }
    public Question(Long id, String question, byte[] image) {
        this.id=id;
        this.name_image=image;
        this.name = question;
        //this.id=createID();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getName_image() {
        return name_image;
    }

    public void setName_image(byte[] name_image) {
        this.name_image = name_image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
       // dest.writeLong(id);
        dest.writeString(name);
        dest.writeInt(name_image.length);
        dest.writeByteArray(name_image);
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
