package br.com.danielpadua.crstracker;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by danielpadua on 13/02/2018.
 */

class Exchange implements Serializable {
    private ArrayList<Pair> supportedPairs;
    private int id;
    private String name;

    Exchange() {
        setSupportedPairs(new ArrayList<Pair>());
    }

    Exchange(ArrayList<Pair> supportedPairs, int id, String name) {
        setSupportedPairs(supportedPairs);
        setId(id);
        setName(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Pair> getSupportedPairs() {
        return supportedPairs;
    }

    public void setSupportedPairs(ArrayList<Pair> supportedPairs) {
        this.supportedPairs = supportedPairs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }



}
