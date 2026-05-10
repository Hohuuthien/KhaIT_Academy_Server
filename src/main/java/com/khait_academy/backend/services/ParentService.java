
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

        validateParentNotExists(request.getUserId());

        User user = getUserOrThrow(request.getUserId());

        Parent parent = ParentMapper.toEntity(request, user);

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
        return ParentMapper.toResponse(getParentOrThrow(id));
    }

    // ================= GET BY USER =================
    @Transactional(readOnly = true)
    public ParentResponse getByUserId(Long userId) {
        return ParentMapper.toResponse(
                parentRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Parent not found")
                        )
        );
    }

    // ================= UPDATE =================
    public ParentResponse update(Long id, ParentRequest request) {

        Parent parent = getParentOrThrow(id);

        ParentMapper.updateEntity(parent, request);

        return ParentMapper.toResponse(
                parentRepository.save(parent)
        );
    }

    // ================= CHANGE STATUS =================
    public ParentResponse changeStatus(Long id, ParentStatus status) {

        Parent parent = getParentOrThrow(id);

        parent.setStatus(status);

        return ParentMapper.toResponse(
                parentRepository.save(parent)
        );
    }

    // ================= DELETE =================
    public void delete(Long id) {

        Parent parent = getParentOrThrow(id);

        if (!parent.getStudents().isEmpty()) {
            throw new BadRequestException(
                    "Cannot delete parent with assigned students"
            );
        }

        parentRepository.delete(parent);
    }

    // ================= HELPERS =================
    private Parent getParentOrThrow(Long id) {
        return parentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Parent not found")
                );
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );
    }

    private void validateParentNotExists(Long userId) {
        if (parentRepository.existsByUserId(userId)) {
            throw new BadRequestException(
                    "Parent already exists for this user"
            );
        }
    }
}

