package com.example.demo.model;

import java.util.List;

public class link {
    public String text;
    public String href;

    public link() {
    }

    public link(String text, String href) {
        this.text = text;
        this.href = href;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
