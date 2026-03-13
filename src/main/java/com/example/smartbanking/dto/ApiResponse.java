package com.example.smartbanking.dto;

public class ApiResponse {

    private String message;   // short message
    private Object data;      // optional payload

    public ApiResponse(String message) {
        this.message = message;
    }

    public ApiResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
