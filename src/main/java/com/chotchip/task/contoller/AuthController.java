package com.chotchip.task.contoller;

import com.chotchip.task.dto.request.UserRequestDTO;
import com.chotchip.task.entity.User;
import com.chotchip.task.security.JwtTokenUtil;
import com.chotchip.task.service.UserService;
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
    public Map<String, String> login(@RequestBody UserRequestDTO user) {
//        User existingUser = new User(1l, "email.com", "1223", Role.ADMIN, List.of());
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
