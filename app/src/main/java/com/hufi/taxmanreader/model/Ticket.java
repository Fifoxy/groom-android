package com.hufi.taxmanreader.model;

/**
 * Created by Pierre Defache on 13/12/2015.
 */
public class Ticket {

    private String ln;
    private String fn;
    private String id;
    private String iat;
    private String prid;

    public String getLastname() {
        return ln;
    }

    private void setLastname(String lastname) {
        this.ln = lastname;
    }

    public String getFirstname() {
        return fn;
    }

    private void setFirstname(String firstname) {
        this.fn = firstname;
    }

    public String getTicket_id() {
        return id;
    }

    private void setTicket_id(String ticket_id) {
        this.id = ticket_id;
    }

    public String getIat() {
        return iat;
    }

    private void setIat(String iat) {
        this.iat = iat;
    }

    public String getPrid() {
        return prid;
    }

    private void setPrid(String prid) {
        this.prid = prid;
    }
}
