package com.jeancaio.financecontrol.controller;

import com.jeancaio.financecontrol.config.JwtUtil;
import com.jeancaio.financecontrol.model.User;
import com.jeancaio.financecontrol.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // ========== CADASTRO ==========
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        try {
            String name = request.get("name");
            String email = request.get("email");
            String password = request.get("password");

            if (name == null || email == null || password == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Todos os campos são obrigatórios"));
            }

            if (userService.findByEmail(email) != null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Email já cadastrado"));
            }

            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));

            User savedUser = userService.save(user);

            // Gerar token JWT real
            String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Usuário cadastrado com sucesso");
            response.put("token", token);
            response.put("user", Map.of(
                    "id", savedUser.getId(),
                    "name", savedUser.getName(),
                    "email", savedUser.getEmail()
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("message", "Erro ao cadastrar usuário: " + e.getMessage()));
        }
    }

    // ========== LOGIN ==========
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");

            System.out.println("📧 Tentativa de login: " + email);

            if (email == null || password == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Email e senha são obrigatórios"));
            }

            User user = userService.findByEmail(email);

            if (user == null) {
                System.out.println("❌ Usuário não encontrado");
                return ResponseEntity.status(401)
                        .body(Map.of("message", "Email ou senha incorretos"));
            }

            System.out.println("👤 Usuário encontrado: " + user.getEmail());

            boolean isPasswordValid = passwordEncoder.matches(password, user.getPassword());
            System.out.println("🔑 Senha válida: " + isPasswordValid);

            if (!isPasswordValid) {
                return ResponseEntity.status(401)
                        .body(Map.of("message", "Email ou senha incorretos"));
            }

            // Gerar token JWT real
            String token = jwtUtil.generateToken(user.getEmail(), user.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login realizado com sucesso");
            response.put("token", token);
            response.put("user", Map.of(
                    "id", user.getId(),
                    "name", user.getName(),
                    "email", user.getEmail()
            ));

            System.out.println("✅ Login realizado com sucesso - Token gerado");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Erro no login: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("message", "Erro ao fazer login: " + e.getMessage()));
        }
    }

    // ========== BUSCAR USUÁRIO ATUAL ==========
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader(value = "Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            String email = jwtUtil.extractUsername(token);

            User user = userService.findByEmail(email);

            if (user == null) {
                return ResponseEntity.status(404)
                        .body(Map.of("message", "Usuário não encontrado"));
            }

            Map<String, Object> response = Map.of(
                    "id", user.getId(),
                    "name", user.getName(),
                    "email", user.getEmail()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(403)
                    .body(Map.of("message", "Token inválido"));
        }
    }
}