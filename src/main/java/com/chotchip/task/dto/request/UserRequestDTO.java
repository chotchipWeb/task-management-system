package com.chotchip.task.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    @Email
    @Schema(description = "Email пользователя", example = "test@email.com")
    private String email;
    @Schema(description = "Пароль пользователя", example = "test")
    private String password;
}
