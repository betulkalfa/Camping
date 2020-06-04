package com.sbk.camping.model;

import com.google.android.gms.tasks.Task;

import java.util.List;

public class Cihaz {

    public Cihaz(String id) {
        this.id = id;
    }

    String id;
    private List<Kamp> kampList;
    private List<Malzeme> malzemeList;

    public Cihaz(String id,List<Kamp>kampList) {
        this.id = id;

        this.kampList=kampList;
    }

    public Cihaz(String id, List<Malzeme>malzemeList,List<Kamp>kampList) {
        this.id = id;
        this.malzemeList=malzemeList;
        this.kampList=kampList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Kamp> getKampList() {
        return kampList;
    }

    public void setKampList(List<Kamp> kampList) {
        this.kampList = kampList;
    }

    public List<Malzeme> getMalzemeList() {
        return malzemeList;
    }

    public void setMalzemeList(List<Malzeme> malzemeList) {
        this.malzemeList = malzemeList;
    }



    public Cihaz(Task<Void> cid, List<Kamp> kampList) {
    }




}
