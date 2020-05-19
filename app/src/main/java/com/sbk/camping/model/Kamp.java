package com.sbk.camping.model;


import java.util.Comparator;
import java.util.List;

public class Kamp {

    private String id;
    private String adi;
    private String turu;
    private List<Malzeme> malzemeList;
    private List<Malzeme> olanMalzemeList;

    public Kamp() {
    }

    public Kamp(String id, String adi, String turu, List<Malzeme> malzemeList) {
        this.id = id;
        this.adi = adi;
        this.turu = turu;
        this.malzemeList = malzemeList;
    }

    public Kamp(String id, String adi, String turu, List<Malzeme> malzemeList, List<Malzeme> olanMalzemeList) {
        this.id = id;
        this.adi = adi;
        this.turu = turu;
        this.malzemeList = malzemeList;
        this.olanMalzemeList = olanMalzemeList;
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

    public List<Malzeme> getMalzemeList() {
        return malzemeList;
    }

    public void setMalzemeList(List<Malzeme> malzemeList) {
        this.malzemeList = malzemeList;
    }

    public List<Malzeme> getOlanMalzemeList() {
        return olanMalzemeList;
    }

    public void setOlanMalzemeList(List<Malzeme> olanMalzemeList) {
        this.olanMalzemeList = olanMalzemeList;
    }


    }

