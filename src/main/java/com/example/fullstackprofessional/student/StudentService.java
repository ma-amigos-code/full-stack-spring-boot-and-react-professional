package com.example.fullstackprofessional.student;

import com.example.fullstackprofessional.student.exception.BadRequestException;
import com.example.fullstackprofessional.student.exception.StudentNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void addStudent(Student student) {
        // check if email is taken
        if (studentRepository.selectExistsEmail(student.getEmail())) {
            throw new BadRequestException("Email "+ student.getEmail() + " taken");
        }
        studentRepository.save(student);
    }

    public void addDelete(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException("Student with id " + studentId + " does not exists");
        }
        studentRepository.deleteById(studentId);
    }
}