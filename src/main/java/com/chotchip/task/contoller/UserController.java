package com.chotchip.task.contoller;

import com.chotchip.task.dto.request.UserRequestDTO;
import com.chotchip.task.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        userService.create(userRequestDTO);
        return ResponseEntity
                .noContent()
                .build();
    }

}
