package com.khait_academy.backend.services;

import com.khait_academy.backend.dto.request.StudentRequest;
import com.khait_academy.backend.dto.response.StudentResponse;
import com.khait_academy.backend.entities.*;
import com.khait_academy.backend.exception.BadRequestException;
import com.khait_academy.backend.exception.ResourceNotFoundException;
import com.khait_academy.backend.mapper.StudentMapper;
import com.khait_academy.backend.repositories.*;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
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

        if (studentRepository.existsByUserId(request.getUserId())) {
            throw new BadRequestException("Student already exists for this user");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        Parent parent = null;

        if (request.getParentId() != null) {
            parent = parentRepository.findById(request.getParentId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Parent not found")
                    );
        }

        Student student = StudentMapper.toEntity(request, user, parent);

        return StudentMapper.toResponse(
                studentRepository.save(student)
        );
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

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found")
                );

        return StudentMapper.toResponse(student);
    }

    // ================= UPDATE =================
    public StudentResponse update(Long id, StudentRequest request) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found")
                );

        Parent parent = null;

        if (request.getParentId() != null) {
            parent = parentRepository.findById(request.getParentId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Parent not found")
                    );
        }

        StudentMapper.updateEntity(student, request, parent);

        return StudentMapper.toResponse(
                studentRepository.save(student)
        );
    }

    // ================= DELETE =================
    public void delete(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found")
                );

        studentRepository.delete(student);
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
}