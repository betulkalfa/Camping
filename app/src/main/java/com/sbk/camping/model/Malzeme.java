package com.sbk.camping.model;


public class Malzeme {
    private String id;
    private String adi;
    private String turu;

    public Malzeme() {
    }

    public Malzeme(String id, String adi, String turu) {
        this.id = id;
        this.adi = adi;
        this.turu = turu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdi() {
        return adi;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public String getTuru() {
        return turu;
    }

    public void setTuru(String turu) {
        this.turu = turu;
    }
}
