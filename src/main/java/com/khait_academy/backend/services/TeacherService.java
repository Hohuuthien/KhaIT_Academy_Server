package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.TeacherRequest;
import com.khait_academy.backend.dto.response.TeacherResponse;
import com.khait_academy.backend.entities.Teacher;
import com.khait_academy.backend.entities.User;
import com.khait_academy.backend.enums.TeacherStatus;
import com.khait_academy.backend.exception.BadRequestException;
import com.khait_academy.backend.exception.ResourceNotFoundException;
import com.khait_academy.backend.mapper.TeacherMapper;
import com.khait_academy.backend.repositories.TeacherRepository;
import com.khait_academy.backend.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;

    // ================= CREATE =================
    public TeacherResponse create(TeacherRequest request) {

        // check user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        // check duplicate teacher
        if (teacherRepository.existsByUserId(user.getId())) {
            throw new BadRequestException("Teacher already exists for this user");
        }

        Teacher teacher = TeacherMapper.toEntity(request, user);

        return TeacherMapper.toResponse(
                teacherRepository.save(teacher)
        );
    }

    // ================= GET ALL =================
    @Transactional(readOnly = true)
    public List<TeacherResponse> getAll() {

        return teacherRepository.findAll()
                .stream()
                .map(TeacherMapper::toResponse)
                .toList();
    }

    // ================= GET BY ID =================
    @Transactional(readOnly = true)
    public TeacherResponse getById(Long id) {

        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher not found")
                );

        return TeacherMapper.toResponse(teacher);
    }

    // ================= UPDATE =================
    public TeacherResponse update(Long id, TeacherRequest request) {

        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher not found")
                );

        User user = null;

        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("User not found")
                    );
        }

        TeacherMapper.updateEntity(teacher, request, user);

        return TeacherMapper.toResponse(
                teacherRepository.save(teacher)
        );
    }

    // ================= DELETE =================
    public void delete(Long id) {

        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher not found")
                );

        teacherRepository.delete(teacher);
    }

    // ================= CHANGE STATUS =================
    public TeacherResponse changeStatus(Long id, String status) {

        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher not found")
                );

        try {
            teacher.setStatus(
                    TeacherStatus.valueOf(status.toUpperCase())
            );
        } catch (Exception e) {
            throw new BadRequestException("Invalid teacher status");
        }

        return TeacherMapper.toResponse(
                teacherRepository.save(teacher)
        );
    }

    // ================= GET BY USER =================
    @Transactional(readOnly = true)
    public TeacherResponse getByUserId(Long userId) {

        Teacher teacher = teacherRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher not found for user")
                );

        return TeacherMapper.toResponse(teacher);
    }
}