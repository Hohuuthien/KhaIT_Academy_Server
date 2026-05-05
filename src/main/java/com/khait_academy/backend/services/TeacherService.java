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

        User user = getUser(request.getUserId());

        validateTeacherNotExists(user.getId());

        Teacher teacher = TeacherMapper.toEntity(request, user);

        return toResponse(teacherRepository.save(teacher));
    }

    // ================= GET ALL =================
    @Transactional(readOnly = true)
    public List<TeacherResponse> getAll() {
        return teacherRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ================= GET BY ID =================
    @Transactional(readOnly = true)
    public TeacherResponse getById(Long id) {
        return toResponse(getTeacher(id));
    }

    // ================= UPDATE =================
    public TeacherResponse update(Long id, TeacherRequest request) {

        Teacher teacher = getTeacher(id);

        User user = null;

        if (request.getUserId() != null) {
            user = getUser(request.getUserId());

            // 🔥 check duplicate khi đổi user
            if (!user.getId().equals(teacher.getUser().getId())) {
                validateTeacherNotExists(user.getId());
            }
        }

        TeacherMapper.updateEntity(teacher, request, user);

        return toResponse(teacherRepository.save(teacher));
    }

    // ================= DELETE =================
    public void delete(Long id) {
        if (!teacherRepository.existsById(id)) {
            throw new ResourceNotFoundException("Teacher not found");
        }
        teacherRepository.deleteById(id);
    }

    // ================= CHANGE STATUS =================
    public TeacherResponse changeStatus(Long id, String status) {

        Teacher teacher = getTeacher(id);

        teacher.setStatus(parseStatus(status));

        return toResponse(teacherRepository.save(teacher));
    }

    // ================= GET BY USER =================
    @Transactional(readOnly = true)
    public TeacherResponse getByUserId(Long userId) {
        return teacherRepository.findByUserId(userId)
                .map(this::toResponse)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher not found for user"));
    }

    // ================= PRIVATE METHODS =================

    private Teacher getTeacher(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void validateTeacherNotExists(Long userId) {
        if (teacherRepository.existsByUserId(userId)) {
            throw new BadRequestException("Teacher already exists for this user");
        }
    }

    private TeacherStatus parseStatus(String status) {
        try {
            return TeacherStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            throw new BadRequestException("Invalid teacher status");
        }
    }

    private TeacherResponse toResponse(Teacher teacher) {
        return TeacherMapper.toResponse(teacher);
    }
}