package com.khait_academy.backend.controllers;

import com.khait_academy.backend.dto.request.ParentRequest;
import com.khait_academy.backend.dto.response.ApiResponse;
import com.khait_academy.backend.dto.response.ParentResponse;
import com.khait_academy.backend.enums.ParentStatus;
import com.khait_academy.backend.services.ParentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parents")
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<ApiResponse<ParentResponse>> create(
            @Valid @RequestBody ParentRequest request
    ) {

        return ResponseEntity.status(201)
                .body(ApiResponse.success(
                        "Create parent success",
                        parentService.create(request)
                ));
    }

    // ================= GET ALL =================
    @GetMapping
    public ResponseEntity<ApiResponse<List<ParentResponse>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Get parents success",
                        parentService.getAll()
                )
        );
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ParentResponse>> getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Get parent success",
                        parentService.getById(id)
                )
        );
    }

    // ================= GET BY USER =================
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<ParentResponse>> getByUserId(
            @PathVariable Long userId
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Get parent by user success",
                        parentService.getByUserId(userId)
                )
        );
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ParentResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ParentRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Update parent success",
                        parentService.update(id, request)
                )
        );
    }

    // ================= CHANGE STATUS =================
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ParentResponse>> changeStatus(
            @PathVariable Long id,
            @RequestParam ParentStatus status
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Change parent status success",
                        parentService.changeStatus(id, status)
                )
        );
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id
    ) {

        parentService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.success("Delete parent success", null)
        );
    }
}

