package br.com.danielpadua.crstracker;

import java.util.ArrayList;

/**
 * Created by danielpadua on 13/02/2018.
 */

public class Exchange {
    private ArrayList<Pair> supportedPairs;
    private String name;

    public Exchange() {
        setSupportedPairs(new ArrayList<Pair>());
    }

    public Exchange(ArrayList<Pair> supportedPairs, String name) {
        setSupportedPairs(supportedPairs);
        setName(name);
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
        StringBuilder sb = new StringBuilder("Exchange: " + getName());
        for (Pair p : getSupportedPairs()) {
            sb.append("CRS/").append(p.getCoin().toString()).append(": ").append(p.getPrice());
        }
        return sb.toString();
    }


}
