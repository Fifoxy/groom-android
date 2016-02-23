package com.hufi.taxmanreader.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private String event_slug;
    private String price;
    private Integer id;
    private String name;

    public String getEvent_slug() {
        return event_slug;
    }

    private void setEvent_slug(String event_slug) {
        this.event_slug = event_slug;
    }

    public String getPrice() {
        return price;
    }

    private void setPrice(String price) {
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    protected Product(Parcel in) {
        event_slug = in.readString();
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
        dest.writeString(event_slug);
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
}