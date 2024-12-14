package com.chotchip.task.dto.request;

import com.chotchip.task.entity.enums.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCreateRequestDTO {
    @NotEmpty
    @NotNull
    @Size(max = 255)
    private String title;
    @Size(max = 255)
    private String details;
    @NotNull
    private Priority priority;
    private List<CommentCreateInTaskRequestDTO> comment;
    @Schema(description = "Email пользователя", example = "test@email.com")
    @Email
    private String executor;
}
