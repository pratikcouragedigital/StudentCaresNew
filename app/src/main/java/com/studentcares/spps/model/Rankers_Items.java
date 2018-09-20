package com.studentcares.spps.model;

import java.util.ArrayList;
import java.util.List;

public class Rankers_Items {

    public String standard;
    public String division;

    public List<Ranker_List_Items> rankerListItems = new ArrayList<Ranker_List_Items>();


    public Rankers_Items() {
    }

    public Rankers_Items(String standard, String division, List<Ranker_List_Items> rankerListItems) {
        this.standard = standard;
        this.division = division;
        this.rankerListItems = rankerListItems;
    }
    public String getstandard() {
        return standard;
    }

    public void setstandard(String standard) {
        this.standard = standard;
    }

    public String getdivision() {
        return division;
    }

    public void setdivision(String division) {
        this.division = division;
    }

    public List<Ranker_List_Items> getRankerListItems() {
        return rankerListItems;
    }

    public void setRankerListItems(List<Ranker_List_Items> rankerListItems) {
        this.rankerListItems = rankerListItems;
    }


}

