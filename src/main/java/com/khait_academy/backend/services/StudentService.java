package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.StudentRequest;
import com.khait_academy.backend.dto.response.StudentResponse;
import com.khait_academy.backend.entities.*;
import com.khait_academy.backend.exception.BadRequestException;
import com.khait_academy.backend.exception.ResourceNotFoundException;
import com.khait_academy.backend.mapper.StudentMapper;
import com.khait_academy.backend.repositories.*;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final ParentRepository parentRepository;

    // ================= CREATE =================
    public StudentResponse create(StudentRequest request) {

        validateStudentNotExists(request.getUserId());

        User user = getUserOrThrow(request.getUserId());
        Parent parent = getParentIfExists(request.getParentId());

        Student student = StudentMapper.toEntity(request, user, parent);

        return StudentMapper.toResponse(studentRepository.save(student));
    }

    // ================= GET ALL =================
    @Transactional(readOnly = true)
    public List<StudentResponse> getAll() {
        return studentRepository.findAll()
                .stream()
                .map(StudentMapper::toResponse)
                .toList();
    }

    // ================= GET BY ID =================
    @Transactional(readOnly = true)
    public StudentResponse getById(Long id) {
        return StudentMapper.toResponse(getStudentOrThrow(id));
    }

    // ================= UPDATE =================
    public StudentResponse update(Long id, StudentRequest request) {

        Student student = getStudentOrThrow(id);
        Parent parent = getParentIfExists(request.getParentId());

        StudentMapper.updateEntity(student, request, parent);

        return StudentMapper.toResponse(studentRepository.save(student));
    }

    // ================= DELETE =================
    public void delete(Long id) {
        studentRepository.delete(getStudentOrThrow(id));
    }

    // ================= GET BY USER =================
    @Transactional(readOnly = true)
    public StudentResponse getByUserId(Long userId) {
        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found")
                );

        return StudentMapper.toResponse(student);
    }

    // ================= PRIVATE HELPERS =================

    private Student getStudentOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found")
                );
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );
    }

    private Parent getParentIfExists(Long parentId) {
        if (parentId == null) return null;

        return parentRepository.findById(parentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Parent not found")
                );
    }

    private void validateStudentNotExists(Long userId) {
        if (studentRepository.existsByUserId(userId)) {
            throw new BadRequestException("Student already exists for this user");
        }
    }
}