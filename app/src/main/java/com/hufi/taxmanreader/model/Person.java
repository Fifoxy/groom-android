package com.hufi.taxmanreader.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable {
    private String first_name;
    private String last_name;
    private Integer id;
    private String email;

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    protected Person(Parcel in) {
        first_name = in.readString();
        last_name = in.readString();
        id = in.readByte() == 0x00 ? null : in.readInt();
        email = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(first_name);
        dest.writeString(last_name);
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        dest.writeString(email);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}