package com.example.demo.model;

import java.util.List;

public class trkseg {
    public List<trkpt> trkpt;

    public trkseg() {
    }

    public trkseg(List<com.example.demo.model.trkpt> trkpt) {
        this.trkpt = trkpt;
    }

    public List<com.example.demo.model.trkpt> getTrkpt() {
        return trkpt;
    }

    public void setTrkpt(List<com.example.demo.model.trkpt> trkpt) {
        this.trkpt = trkpt;
    }
}
