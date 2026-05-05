package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.DiscountRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.DiscountResponse;
import com.khait_academy.backend.services.DiscountService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
@Validated
public class DiscountController {

    private final DiscountService discountService;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<ApiResponse<DiscountResponse>> create(
            @Valid @RequestBody DiscountRequest request
    ) {
        DiscountResponse response = discountService.create(request);

        return ResponseEntity
                .created(URI.create("/api/discounts/" + response.getId()))
                .body(success("Create discount successfully", response));
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DiscountResponse>> update(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody DiscountRequest request
    ) {
        return ResponseEntity.ok(
                success("Update discount successfully",
                        discountService.update(id, request))
        );
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @Min(1) Long id
    ) {
        discountService.delete(id);
        return ResponseEntity.noContent().build(); // chuẩn REST
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DiscountResponse>> getById(
            @PathVariable @Min(1) Long id
    ) {
        return ResponseEntity.ok(
                success("Get discount successfully",
                        discountService.getById(id))
        );
    }

    // ================= GET BY COURSE =================
    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<DiscountResponse>>> getByCourse(
            @PathVariable @Min(1) Long courseId
    ) {
        return ResponseEntity.ok(
                success("Get discounts by course successfully",
                        discountService.getByCourse(courseId))
        );
    }

    // ================= GET ACTIVE =================
    @GetMapping("/course/{courseId}/active")
    public ResponseEntity<ApiResponse<List<DiscountResponse>>> getActive(
            @PathVariable @Min(1) Long courseId
    ) {
        return ResponseEntity.ok(
                success("Get active discounts successfully",
                        discountService.getActiveByCourse(courseId))
        );
    }

    // ================= FINAL PRICE =================
    @GetMapping("/course/{courseId}/final-price")
    public ResponseEntity<ApiResponse<BigDecimal>> getFinalPrice(
            @PathVariable @Min(1) Long courseId
    ) {
        return ResponseEntity.ok(
                success("Get final price successfully",
                        discountService.getFinalPrice(courseId))
        );
    }

    // ================= COMMON RESPONSE =================
    private <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }
}