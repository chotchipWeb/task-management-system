package com.chotchip.task.dto.request;

import com.chotchip.task.entity.enums.Priority;
import com.chotchip.task.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateRequestDTO {
    private String title;
    private String details;
    private Status status;
    private Priority priority;
}
