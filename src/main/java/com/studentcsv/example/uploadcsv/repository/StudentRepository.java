package com.studentcsv.example.uploadcsv.repository;

import com.studentcsv.example.uploadcsv.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Integer> {
}
