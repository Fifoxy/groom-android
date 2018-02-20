package com.hufi.taxmanreader.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable, Comparable{

    private Integer id;
    private String event;
    private String name;
    private String price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEvent() { return event; }

    public void setEvent(String event) { this.event = event; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() { return price; }

    public void setPrice(String price) { this.price = price; }

    protected Product(Parcel in) {
        event = in.readString();
        price = in.readString();
        id = in.readByte() == 0x00 ? null : in.readInt();
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(event);
        dest.writeString(price);
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        dest.writeString(name);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int compareTo(Object another) {
        return this.getId() -  ((Product) another).getId();
    }
}