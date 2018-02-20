package com.hufi.taxmanreader.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Ticket implements Parcelable {
    private int id;
    private boolean used;
    private int order;
    private String first_name;
    private String last_name;

    private Product product;

    private String error;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getPerson_id() {
        return person_id;
    }

    public void setPerson_id(int person_id) {
        this.person_id = person_id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    protected Ticket(Parcel in) {
        id = in.readInt();
        used = in.readByte() != 0x00;
        order = in.readInt();
        first_name = in.readInt();
        last_name = in.readInt();
        product = (Product) in.readValue(Product.class.getClassLoader());
        person = (Person) in.readValue(Person.class.getClassLoader());
        error = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByte((byte) (used ? 0x01 : 0x00));
        dest.writeInt(order);
        dest.writeInt(first_name);
        dest.writeInt(last_name);
        dest.writeValue(product);
        dest.writeValue(person);
        dest.writeString(error);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Ticket> CREATOR = new Parcelable.Creator<Ticket>() {
        @Override
        public Ticket createFromParcel(Parcel in) {
            return new Ticket(in);
        }

        @Override
        public Ticket[] newArray(int size) {
            return new Ticket[size];
        }
    };
}