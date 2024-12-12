package com.chotchip.task.dto.request;

import com.chotchip.task.entity.enums.Priority;
import com.chotchip.task.entity.enums.Status;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateRequestDTO {
    @NotNull
    @NotEmpty
    private String title;
    private String details;
    @NotEmpty
    @NotNull
    private Status status;
    @NotNull
    private Priority priority;
}
