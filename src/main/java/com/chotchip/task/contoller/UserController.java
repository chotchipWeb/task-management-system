package com.chotchip.task.contoller;

import com.chotchip.task.dto.request.UserRequestDTO;
import com.chotchip.task.dto.response.UserResponseDTO;
import com.chotchip.task.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @Operation(summary = "Создание пользователя",description = "Пользователь создается с правами client")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO,
                                                      UriComponentsBuilder uriBuilder) {
        UserResponseDTO userDTO = userService.create(userRequestDTO);
        return ResponseEntity
                .created(uriBuilder
                        .replacePath("/api/user/{id}")
                        .build(Map.of("id", userDTO.getEmail())))
                .body(userDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Получить пользователя по id",description = "Доступна только с правами Администратора")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

}
