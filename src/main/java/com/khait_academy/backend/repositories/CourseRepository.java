package com.khait_academy.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khait_academy.backend.entities.Course;

public interface CourseRepository extends JpaRepository <Course, Long>{

}
