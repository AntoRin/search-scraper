package com.antorin.stack_crawler.api;

public class ApiResponse<T> {
    private String status;
    private T data;

    public ApiResponse(String status, T data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return this.status;
    }

    public T getData() {
        return this.data;
    }
}
