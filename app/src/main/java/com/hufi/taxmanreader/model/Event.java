package com.hufi.taxmanreader.model;

import java.util.List;

/**
 * Created by Pierre Defache on 13/12/2015.
 */
public class Event {
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
}
