package com.hufi.taxmanreader.model;

public class QRTicket {

    private String ln;
    private String fn;
    private String id;
    private String iat;
    private String prid;
    private String orid;

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
