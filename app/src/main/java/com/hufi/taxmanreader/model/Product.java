package com.hufi.taxmanreader.model;

public class Product {
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
}
