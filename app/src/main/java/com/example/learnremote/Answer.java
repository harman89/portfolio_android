package com.example.learnremote;

import android.os.Parcel;
import android.os.Parcelable;

public class Answer implements Parcelable {
    private int isTrue;
    private String text;

    protected Answer(Parcel in) {
        isTrue = in.readInt();
        text = in.readString();
    }

    public static final Creator<Answer> CREATOR = new Creator<Answer>() {
        @Override
        public Answer createFromParcel(Parcel in) {
            return new Answer(in);
        }

        @Override
        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };

    public Answer() {

    }

    public String getText()
    {
        return text;
    }
    public int getIsTrue()
    {
        return isTrue;
    }
    public void setText(String text)
    {
        this.text=text;
    }
    public void setIsTrue(int isTrue)
    {
        this.isTrue=isTrue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(isTrue);
        dest.writeString(text);
    }
}
