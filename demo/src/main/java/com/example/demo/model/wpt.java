package com.example.demo.model;


public class wpt {
    public String name;
    public String sym;
    public double lat;
    public double lon;
    public String text;

    public wpt() {
    }

    public wpt(String name, String sym, double lat, double lon, String text) {
        this.name = name;
        this.sym = sym;
        this.lat = lat;
        this.lon = lon;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSym() {
        return sym;
    }

    public void setSym(String sym) {
        this.sym = sym;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
