package com.example.demo.model;

import java.util.Date;

public class trkpt {
    public double ele;
    public Date time;
    public double lat;
    public double lon;
    public String text;

    public trkpt() {
    }

    public trkpt(double ele, Date time, double lat, double lon, String text) {
        this.ele = ele;
        this.time = time;
        this.lat = lat;
        this.lon = lon;
        this.text = text;
    }

    public double getEle() {
        return ele;
    }

    public void setEle(double ele) {
        this.ele = ele;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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
