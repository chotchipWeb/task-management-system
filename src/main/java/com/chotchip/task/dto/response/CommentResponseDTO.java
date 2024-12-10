package com.chotchip.task.dto.response;

import com.chotchip.task.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO {
    private String details;
    private UserResponseTaskDTO author;
}
