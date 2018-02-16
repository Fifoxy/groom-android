package fr.galaisen.groomreader.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Event implements Parcelable {
    private List<Product> products;
    private String name;
    private Place place;

    private String contact_mail;
    private Integer place_id;
    private String contact_phone;
    private String slug;
    private String description;

    public List<Product> getProducts() {
        return products;
    }

    public String getName() {
        return name;
    }

    public Place getPlace() {
        return place;
    }

    public String getContact_mail() {
        return contact_mail;
    }

    public Integer getPlace_id() {
        return place_id;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public String getSlug() {
        return slug;
    }

    public String getDescription() {
        return description;
    }


    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public void setContact_mail(String contact_mail) {
        this.contact_mail = contact_mail;
    }

    public void setPlace_id(Integer place_id) {
        this.place_id = place_id;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Event(){
        place = new Place();
    }

    protected Event(Parcel in) {
        if (in.readByte() == 0x01) {
            products = new ArrayList<Product>();
            in.readList(products, Product.class.getClassLoader());
        } else {
            products = null;
        }
        name = in.readString();
        place = (Place) in.readValue(Place.class.getClassLoader());
        contact_mail = in.readString();
        place_id = in.readByte() == 0x00 ? null : in.readInt();
        contact_phone = in.readString();
        slug = in.readString();
        description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (products == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(products);
        }
        dest.writeString(name);
        dest.writeValue(place);
        dest.writeString(contact_mail);
        if (place_id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(place_id);
        }
        dest.writeString(contact_phone);
        dest.writeString(slug);
        dest.writeString(description);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}