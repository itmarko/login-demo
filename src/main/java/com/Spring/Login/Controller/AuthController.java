package com.Spring.Login.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Spring.Login.Service.StudentService;
import com.Spring.Login.Service.UserService;
import com.Spring.Login.User.Student;
import com.Spring.Login.User.User;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        try {
            userService.register(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user, HttpServletRequest request) {
        return userService.login(user.getUsername(), user.getPassword())
            .map(u -> {
                request.getSession().setAttribute("user", u);
                return ResponseEntity.ok("Login successful");
            })
            .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials"));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/students")
    public ResponseEntity<String> addStudent(@RequestBody Student student, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user"); // Get the logged-in user
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        try {
            studentService.addStudent(student, user);
            return ResponseEntity.ok("Student added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding student");
        }
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getStudents(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user"); // Get the logged-in user
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<Student> students = studentService.getAllStudentsByUser(user);
        return ResponseEntity.ok(students);
    }
}
