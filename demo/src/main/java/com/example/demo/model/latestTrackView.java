package com.example.demo.model;

public class latestTrackView {
    Long gpx_id;
    String name;
    String description;

    public latestTrackView(Long gpx_id, String name, String description) {
        this.gpx_id = gpx_id;
        this.name = name;
        this.description = description;
    }

    public latestTrackView() {
    }

    public Long getGpx_id() {
        return gpx_id;
    }

    public void setGpx_id(Long gpx_id) {
        this.gpx_id = gpx_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
