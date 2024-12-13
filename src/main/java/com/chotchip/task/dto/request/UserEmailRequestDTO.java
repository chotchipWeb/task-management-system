package com.chotchip.task.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEmailRequestDTO {
    @Schema(description = "Email пользователя", example = "test@email.com")
    @Email
    private String email;
}
