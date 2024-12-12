package com.chotchip.task.contoller;

import com.chotchip.task.dto.request.UserRequestDTO;
import com.chotchip.task.entity.User;
import com.chotchip.task.security.JwtTokenUtil;
import com.chotchip.task.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "Получить токен аутентификации", description = "Использовать в аутентификации")
    public Map<String, String> login(@RequestBody UserRequestDTO user) {
        User existingUser = userService.getUserByEmail(user.getEmail());

        if (existingUser.getPassword().equals(user.getPassword())) {
            String token = JwtTokenUtil.generateToken(existingUser.getEmail());
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return response;
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

}
