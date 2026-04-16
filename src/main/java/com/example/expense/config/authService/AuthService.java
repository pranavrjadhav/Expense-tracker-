package com.example.expense.config.authService;


import com.example.expense.config.auth.JwtUtil;
import com.example.expense.dtos.AuthResponse;
import com.example.expense.dtos.LoginRequest;
import com.example.expense.dtos.RegisterRequest;
import com.example.expense.entity.User;
import com.example.expense.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public void register(RegisterRequest req) {

        if (userRepository.findByEmail(req.email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .username(req.username)
                .email(req.email)
                .password(passwordEncoder.encode(req.password))
                .build();

        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest req) {

        User user = userRepository.findByEmail(req.email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(req.password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(token);
    }
}