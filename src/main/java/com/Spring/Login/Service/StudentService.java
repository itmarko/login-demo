package com.Spring.Login.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Spring.Login.Repo.StudentRepository;
import com.Spring.Login.User.Student;
import com.Spring.Login.User.User;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student addStudent(Student student, User user) {
        student.setUser(user); // Associate the student with the logged-in user
        return studentRepository.save(student);
    }

    public List<Student> getAllStudentsByUser(User user) {
        return studentRepository.findByUserId(user.getId());
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public boolean deleteStudent(Long id, User user) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent() && student.get().getUser().getId().equals(user.getId())) {
            studentRepository.delete(student.get());
            return true;
        }
        return false;
    }
}
