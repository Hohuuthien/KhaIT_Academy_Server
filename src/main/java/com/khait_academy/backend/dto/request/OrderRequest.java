package com.khait_academy.backend.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    private List<Long> courseIds;
    private String paymentMethod;
}