package com.khait_academy.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {

    private boolean success;   // true / false
    private String message;    // thông báo
    private T data;            // dữ liệu trả về
}