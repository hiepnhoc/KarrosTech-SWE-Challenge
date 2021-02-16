package com.example.demo.model;

public class trk {
    public trkseg trkseg;

    public trk() {
    }

    public trk(com.example.demo.model.trkseg trkseg) {
        this.trkseg = trkseg;
    }

    public com.example.demo.model.trkseg getTrkseg() {
        return trkseg;
    }

    public void setTrkseg(com.example.demo.model.trkseg trkseg) {
        this.trkseg = trkseg;
    }
}
