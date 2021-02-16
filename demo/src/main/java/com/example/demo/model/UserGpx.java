package com.example.demo.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "gpx")
public class UserGpx {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long gpx_id;

    String username;

    @Lob
    @Column(name = "gpx", columnDefinition="CLOB")
    private String gpx;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "document_format")
    private String documentFormat;

    @Column(name = "upload_dir")
    private String uploadDir;

    private Timestamp createdDate;

    private String createdBy;

    public UserGpx() {
    }

    public UserGpx(String username, String gpx) {
        this.username = username;
        this.gpx = gpx;
    }

    public Long getGpx_id() {
        return gpx_id;
    }

    public void setGpx_id(Long gpx_id) {
        this.gpx_id = gpx_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGpx() {
        return gpx;
    }

    public void setGpx(String gpx) {
        this.gpx = gpx;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDocumentFormat() {
        return documentFormat;
    }

    public void setDocumentFormat(String documentFormat) {
        this.documentFormat = documentFormat;
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
