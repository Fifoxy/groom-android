package com.hufi.taxmanreader.model;

/**
 * Created by Pierre Defache on 13/12/2015.
 */
public class Place {
    private String address;
    private Integer id;
    private String name;

    public String getAddress() {
        return address;
    }

    private void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }
}
