package com.chotchip.task.dto.request;

import com.chotchip.task.entity.Comment;
import com.chotchip.task.entity.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCreateRequestDTO {
    private String title;
    private String details;
    private Priority priority;
    private List<CommentCreateInTaskRequestDTO> comment;
    private String executor;
}
