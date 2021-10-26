package com.example.learnremote;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Part implements Parcelable {
    private int number;
    private int part_id;
    private String text;
    private ArrayList<Question> questions;


    public Part() {

    }

    protected Part(Parcel in) {
        number = in.readInt();
        part_id = in.readInt();
        text = in.readString();
        questions = in.createTypedArrayList(Question.CREATOR);
    }

    public static final Creator<Part> CREATOR = new Creator<Part>() {
        @Override
        public Part createFromParcel(Parcel in) {
            return new Part(in);
        }

        @Override
        public Part[] newArray(int size) {
            return new Part[size];
        }
    };

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public void setPart_id(int part_id) {
        this.part_id = part_id;
    }

    public int getPart_id() {
        return part_id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(number);
        dest.writeInt(part_id);
        dest.writeString(text);
        dest.writeTypedList(questions);
    }
}
