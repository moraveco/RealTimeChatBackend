package com.moraveco.springboot.controller;

import com.moraveco.springboot.entity.Login;
import com.moraveco.springboot.entity.Register;
import com.moraveco.springboot.repository.LoginRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final LoginRepository loginRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthController(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    @PostMapping("/register")
    public String register(@RequestBody Register register) {
        if (loginRepository.existsByEmail(register.getEmail())) {
            return "Email already registered.";
        }
        register.setPassword(passwordEncoder.encode(register.getPassword()));
        loginRepository.saveLogin(register.getEmail(), register.getPassword());
        loginRepository.saveUser(UUID.randomUUID().toString(), register.getName(), register.getLastname());
        Login stored = loginRepository.findByEmail(register.getEmail());
        return "User registered successfully: " + stored.getId();
    }

    @PostMapping("/login")
    public String login(@RequestBody Login login) {
        Login stored = loginRepository.findByEmail(login.getEmail());
        if (stored == null || !passwordEncoder.matches(login.getPassword(), stored.getPassword())) {
            return "Invalid email or password.";
        }
        return "Login successful: " + stored.getId();
    }
}
