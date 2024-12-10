package com.chotchip.task.dto.request;

import com.chotchip.task.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateStatusRequestDTO {
    private Status status;
}
