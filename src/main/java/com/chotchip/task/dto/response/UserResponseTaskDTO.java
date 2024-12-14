package com.chotchip.task.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseTaskDTO {
    @Schema(description = "Email пользователя", example = "test@email.com")
    private String email;
}
