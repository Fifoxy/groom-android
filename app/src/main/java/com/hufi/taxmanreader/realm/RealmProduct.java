package com.hufi.taxmanreader.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmProduct extends RealmObject {
    @PrimaryKey
    private Integer id;
    private String name;
    private String price;
    private RealmEvent event;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public RealmEvent getEvent() {
        return event;
    }

    public void setEvent(RealmEvent event) {
        this.event = event;
    }
}
