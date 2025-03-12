package com.chotchip.task.contoller;

import com.chotchip.task.dto.request.UserRequestDTO;
import com.chotchip.task.dto.response.ExceptionResponseDTO;
import com.chotchip.task.dto.response.TokenResponse;
import com.chotchip.task.entity.User;
import com.chotchip.task.execption.UserInvalidCredentialsException;
import com.chotchip.task.security.JwtTokenUtil;
import com.chotchip.task.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "Получить токен аутентификации", description = "Использовать в аутентификации ADMIN admin@email.com:admin, CLIENT test@email.com:test",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Возвращен токен"),
                    @ApiResponse(responseCode = "400", description = "Не правильные данные: почта или пароль",
                            content = @Content(
                                    schema = @Schema(implementation = ExceptionResponseDTO.class)
                            ))
            }
    )
    public ResponseEntity<TokenResponse> login(@RequestBody UserRequestDTO user) {
        User existingUser = userService.getUserByEmail(user.getEmail());

        if (existingUser.getPassword().equals(user.getPassword())) {
            String token = JwtTokenUtil.generateToken(existingUser.getEmail());

            return ResponseEntity.ok(new TokenResponse(token));
        } else {
            throw new UserInvalidCredentialsException();
        }
    }

}
