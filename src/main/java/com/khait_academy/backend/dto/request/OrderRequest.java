package com.khait_academy.backend.dto.request;

import com.khait_academy.backend.enums.PaymentMethod;
import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {

    // ===== COURSES =====
    @NotEmpty(message = "Danh sách khóa học không được rỗng")
    private List<
            @NotNull(message = "CourseId không được null")
            Long
    > courseIds;

    // ===== PAYMENT =====
    @NotNull(message = "Payment method không được để trống")
    private PaymentMethod paymentMethod;
}