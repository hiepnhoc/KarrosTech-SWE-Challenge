package com.example.demo.model;

import javax.persistence.*;
import java.util.List;


public class gpx {

    public metadata metadata;
    public List<wpt> wpt;
    public trk trk;
    public String xmlns;
    public String version;
    public String creator;
    public String text;

    public gpx() {
    }

    public gpx(com.example.demo.model.metadata metadata, List<com.example.demo.model.wpt> wpt, com.example.demo.model.trk trk, String xmlns, String version, String creator, String text) {
        this.metadata = metadata;
        this.wpt = wpt;
        this.trk = trk;
        this.xmlns = xmlns;
        this.version = version;
        this.creator = creator;
        this.text = text;
    }

    public com.example.demo.model.metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(com.example.demo.model.metadata metadata) {
        this.metadata = metadata;
    }

    public List<com.example.demo.model.wpt> getWpt() {
        return wpt;
    }

    public void setWpt(List<com.example.demo.model.wpt> wpt) {
        this.wpt = wpt;
    }

    public com.example.demo.model.trk getTrk() {
        return trk;
    }

    public void setTrk(com.example.demo.model.trk trk) {
        this.trk = trk;
    }

    public String getXmlns() {
        return xmlns;
    }

    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}


