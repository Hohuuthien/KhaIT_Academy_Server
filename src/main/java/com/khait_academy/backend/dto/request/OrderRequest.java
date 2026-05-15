package com.khait_academy.backend.dto.request;

import com.khait_academy.backend.enums.PaymentMethod;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    @NotEmpty(message = "Course ids cannot be empty")
    private List<Long> courseIds;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
}