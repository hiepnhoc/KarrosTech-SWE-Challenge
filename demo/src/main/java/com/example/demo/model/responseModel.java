package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

public class responseModel<T> implements Serializable {
    private String request_id;
    private String reference_id;
    private Timestamp date_time;
    private int status;

    private int result_code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private T data;

    public static String ERROR = "Other errors";
    public static String SUCCESS = "SUCCESS";
    public static String EXISTED_RECORD = "Record đã tồn tại";
    public static String NONE_PROCCESSING = "None Proccessing";
    public static String NO_RECORD = "Không có kết quả";
    public static String UPDATE_FAILED = "Cập nhật thất bại";
    public static String ADD_USER_DATA_REQUIRED = "Add User Failed. Please fill all data of User.";
    public static String ADD_USER_ROLE_INVALID = "Invalid role to action";
    public static String USERNAME_REQUIRED = "User name is mandatory !";
    public static String USERNAME_EXISTED = "ADD USER FAIL: USER EXIST IN TEAM NAME";
    public static String ADD_USER_FAILED = "NOT PERMISSION";
    public static String ADD_USER_SUCCESS = "ADD USER SUCCESS !";

    public responseModel(int status, String requestId, String message) {
        this.status = status;
        this.reference_id = requestId;
        this.message = message;
    }
    public responseModel(int status, String requestId, T data, String message) {
        this.status = status;
        this.reference_id = requestId;
        this.message = message;
        this.data = data;

    }
    public responseModel(int status, String requestId, T data, String message, long total, int totalpage) {
        this.status = status;
        this.reference_id = requestId;
        this.message = message;
        this.data = (T) Map.of("result",data,"total", total,"totalpage",totalpage);
    }

    public responseModel() {
        this.setResult_code(0);
    }

    public responseModel(T data) {
        this();
        this.data = data;
    }


    public String getRequest_id() {
        return request_id;
    }

    public responseModel setRequest_id(String request_id) {
        this.request_id = request_id;
        return this;
    }

    public String getReference_id() {
        return reference_id;
    }

    public responseModel setReference_id(String reference_id) {
        this.reference_id = reference_id;
        return this;
    }

    public String getDate_time() {
        Instant now = Instant.now();
        ZoneId zoneId = ZoneId.of("Asia/Bangkok"); //Etc/GMT-7  Asia/Bangkok
        return ZonedDateTime.ofInstant(now, zoneId).toOffsetDateTime().toString();
    }

    public responseModel setDate_time(Timestamp date_time) {
        this.date_time = date_time;
        return this;
    }

    public int getResult_code() {
        return result_code;
    }

    public responseModel setResult_code(int result_code) {
        this.result_code = result_code;
        return this;
    }

    public T getData() {
        return data;
    }

    public responseModel setData(T data) {
        this.data = data;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public responseModel setMessage(String message) {
        this.message = message;
        return this;
    }

    public responseModel setMessage(String format, String... params) {
        if (params != null && params.length > 0) {
            this.message = String.format(format, (Object[]) params);
        }
        return this;
    }

    public responseModel setFailMessage(String format, String... params) {
        this.setResult_code(500);
        this.setData(null);
        if (params != null && params.length > 0) {
            this.message = String.format(format, (Object[]) params);
        } else {
            this.message = format;
        }
        return this;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public Map<String, Object> toMap() {
        return Map.of("status", this.result_code, "data", this);
    }
}
