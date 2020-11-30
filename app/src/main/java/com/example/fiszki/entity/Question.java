package com.example.fiszki.entity;

import android.net.Uri;
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
    private Uri  name_image;
    String extensionImg;

    public Question() {
    }

    public Question(Parcel in) {
        id=in.readLong();
        name=in.readString();
        name_image = in.readParcelable(Uri.class.getClassLoader());
        extensionImg=in.readString();
    }

    public Question(String question) {
        this.name = question;
       // this.id=createID();
    }

    public Question(String question, Uri image, String extensionImg) {
        this.name_image=image;
        this.name = question;
        this.extensionImg=extensionImg;
       // this.id=createID();
    }
    public Question(String question, Uri image) {
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

    public Uri getName_image() {
        return name_image;
    }

    public void setName_image(Uri name_image) {
        this.name_image = name_image;
    }

    public String getExtensionImg() {
        return extensionImg;
    }

    public void setExtensionImg(String extensionImg) {
        this.extensionImg = extensionImg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeParcelable(name_image, flags);
        dest.writeString(extensionImg);
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in)
        {
            return new Question((in));
        }
        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}
