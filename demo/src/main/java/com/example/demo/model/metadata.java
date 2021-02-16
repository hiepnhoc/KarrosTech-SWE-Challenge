package com.example.demo.model;

import java.util.Date;

public class metadata {
    public String name;
    public String desc;
    public Object author;
    public link link;
    public Date time;

    public metadata() {
    }

    public metadata(String name, String desc, Object author, com.example.demo.model.link link, Date time) {
        this.name = name;
        this.desc = desc;
        this.author = author;
        this.link = link;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Object getAuthor() {
        return author;
    }

    public void setAuthor(Object author) {
        this.author = author;
    }

    public com.example.demo.model.link getLink() {
        return link;
    }

    public void setLink(com.example.demo.model.link link) {
        this.link = link;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
