package com.example.fullstackprofessional.student;

import com.example.fullstackprofessional.student.exception.BadRequestException;
import com.example.fullstackprofessional.student.exception.StudentNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

//import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    //private AutoCloseable autoCloseable;
    private StudentService underTest;

    @BeforeEach
    void setUp() {
        //autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new StudentService(studentRepository);
    }

    /*@AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }*/

    @Test
    void canGetAllStudents() {
        // when
        underTest.getAllStudents();

        // then
        verify(studentRepository).findAll();
    }


    @Test
    void canAddStudent() {
        // given
        String email = "jamila@gmail.com";
        Student student = new Student(
                "Jamila",
                email,
                Gender.FEMALE);

        // when
        underTest.addStudent(student);

        // then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);

        verify(studentRepository).save(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void willThrowWhenEmailIsTaken() {
        // given
        String email = "jamila@gmail.com";
        Student student = new Student(
                "Jamila",
                email,
                Gender.FEMALE);

        given(studentRepository.selectExistsEmail(anyString()))
                .willReturn(true);

        // when
        // then
        assertThatThrownBy( () -> underTest.addStudent(student) )
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email "+ student.getEmail() + " taken");

        verify(studentRepository, never()).save(any());

    }

    @Test
    void canDeleteStudent() {
        // given
        Long studentId = 1l;

        given(studentRepository.existsById(any()))
                .willReturn(true);

        // when
        underTest.deleteStudent(studentId);

        // then
        verify(studentRepository).deleteById(studentId);

    }

    @Test
    void willThrowWhenStudentIdDoesNotExists() {
        // given
        Long studentId = 1l;

        given(studentRepository.existsById(any()))
                .willReturn(false);

        // when
        // then
        assertThatThrownBy( () -> underTest.deleteStudent(studentId) )
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with id " + studentId + " does not exists");

        verify(studentRepository, never()).deleteById(any());

    }

}