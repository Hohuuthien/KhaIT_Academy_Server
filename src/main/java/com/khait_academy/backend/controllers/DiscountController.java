package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.DiscountRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.DiscountResponse;
import com.khait_academy.backend.services.DiscountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<ApiResponse<DiscountResponse>> create(
            @Valid @RequestBody DiscountRequest request
    ) {
        return ResponseEntity.status(201).body(
                ApiResponse.<DiscountResponse>builder()
                        .success(true)
                        .message("Create discount success")
                        .data(discountService.create(request))
                        .build()
        );
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DiscountResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody DiscountRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.<DiscountResponse>builder()
                        .success(true)
                        .message("Update discount success")
                        .data(discountService.update(id, request))
                        .build()
        );
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        discountService.delete(id);
        return ResponseEntity.noContent().build(); // giống Category
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DiscountResponse>> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                ApiResponse.<DiscountResponse>builder()
                        .success(true)
                        .message("Get discount success")
                        .data(discountService.getById(id))
                        .build()
        );
    }

    // ================= GET BY COURSE =================
    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<DiscountResponse>>> getByCourse(
            @PathVariable Long courseId
    ) {
        return ResponseEntity.ok(
                ApiResponse.<List<DiscountResponse>>builder()
                        .success(true)
                        .message("Get discounts by course success")
                        .data(discountService.getByCourse(courseId))
                        .build()
        );
    }

    // ================= GET ACTIVE =================
    @GetMapping("/course/{courseId}/active")
    public ResponseEntity<ApiResponse<List<DiscountResponse>>> getActive(
            @PathVariable Long courseId
    ) {
        return ResponseEntity.ok(
                ApiResponse.<List<DiscountResponse>>builder()
                        .success(true)
                        .message("Get active discounts success")
                        .data(discountService.getActiveByCourse(courseId))
                        .build()
        );
    }

    // ================= FINAL PRICE =================
    @GetMapping("/course/{courseId}/final-price")
    public ResponseEntity<ApiResponse<BigDecimal>> getFinalPrice(
            @PathVariable Long courseId
    ) {
        return ResponseEntity.ok(
                ApiResponse.<BigDecimal>builder()
                        .success(true)
                        .message("Get final price success")
                        .data(discountService.getFinalPrice(courseId))
                        .build()
        );
    }
}