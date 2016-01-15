package com.hufi.taxmanreader.model;

import java.util.List;

/**
 * Created by Pierre Defache on 14/12/2015.
 */
public class Order {
    private List<Ticket> tickets;
    private Product product;
    private Person person;

    private String date;

    private Boolean finished;
    private Boolean paid;
    private Boolean revoked;

    private Integer id;
    private String transaction_id;
    private Integer product_id;
    private Integer person_id;


    public List<Ticket> getTickets() {
        return tickets;
    }

    public Product getProduct() {
        return product;
    }

    public Person getPerson() {
        return person;
    }

    public String getDate() {
        return date;
    }

    public Boolean getFinished() {
        return finished;
    }

    public Boolean getPaid() {
        return paid;
    }

    public Boolean getRevoked() { return revoked; }

    public Integer getId() {
        return id;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public Integer getPerson_id() {
        return person_id;
    }
}
