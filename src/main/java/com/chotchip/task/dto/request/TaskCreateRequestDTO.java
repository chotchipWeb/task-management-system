package com.chotchip.task.dto.request;

import com.chotchip.task.entity.enums.Priority;
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
    private String executor;
}
