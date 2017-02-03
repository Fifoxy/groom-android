package com.hufi.taxmanreader.model;

public class QRTicket {

    public String ln;
    public String fn;
    public String id;
    public String iat;
    public String prid;
    public String orid;

    public String getLastname() {
        return ln;
    }

    public String getFirstname() {
        return fn;
    }

    public String getTicket_id() {
        return id;
    }

    public String getIat() {
        return iat;
    }

    public String getPrid() {
        return prid;
    }

    public String getOrid() {
        return orid;
    }
}
