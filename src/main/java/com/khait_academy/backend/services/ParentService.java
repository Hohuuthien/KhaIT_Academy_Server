package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.ParentRequest;
import com.khait_academy.backend.dto.response.ParentResponse;
import com.khait_academy.backend.entities.Parent;
import com.khait_academy.backend.entities.User;
import com.khait_academy.backend.enums.ParentStatus;
import com.khait_academy.backend.exception.BadRequestException;
import com.khait_academy.backend.exception.ResourceNotFoundException;
import com.khait_academy.backend.mapper.ParentMapper;
import com.khait_academy.backend.repositories.ParentRepository;
import com.khait_academy.backend.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ParentService {

    private final ParentRepository parentRepository;
    private final UserRepository userRepository;

    // ================= CREATE =================
    public ParentResponse create(ParentRequest request) {

        if (parentRepository.existsByUserId(request.getUserId())) {
            throw new BadRequestException("Parent already exists for this user");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        Parent parent = ParentMapper.toEntity(request);
        parent.setUser(user);

        return ParentMapper.toResponse(
                parentRepository.save(parent)
        );
    }

    // ================= GET ALL =================
    @Transactional(readOnly = true)
    public List<ParentResponse> getAll() {

        return parentRepository.findAll()
                .stream()
                .map(ParentMapper::toResponse)
                .toList();
    }

    // ================= GET BY ID =================
    @Transactional(readOnly = true)
    public ParentResponse getById(Long id) {

        Parent parent = parentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Parent not found")
                );

        return ParentMapper.toResponse(parent);
    }

    // ================= UPDATE =================
    public ParentResponse update(Long id, ParentRequest request) {

        Parent parent = parentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Parent not found")
                );

        ParentMapper.updateEntity(parent, request);

        return ParentMapper.toResponse(
                parentRepository.save(parent)
        );
    }

    // ================= DELETE =================
    public void delete(Long id) {

        Parent parent = parentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Parent not found")
                );

        parentRepository.delete(parent);
    }

    // ================= GET BY USER =================
    @Transactional(readOnly = true)
    public ParentResponse getByUserId(Long userId) {

        Parent parent = parentRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Parent not found")
                );

        return ParentMapper.toResponse(parent);
    }
}