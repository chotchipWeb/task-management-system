package com.chotchip.task.service;

import com.chotchip.task.dto.request.CommentCreateInTaskRequestDTO;
import com.chotchip.task.dto.request.CommentCreateRequestDTO;
import com.chotchip.task.dto.response.CommentResponseDTO;
import com.chotchip.task.dto.response.TaskResponseDTO;
import com.chotchip.task.dto.response.UserResponseTaskDTO;
import com.chotchip.task.entity.Comment;
import com.chotchip.task.entity.Task;
import com.chotchip.task.entity.User;
import com.chotchip.task.mapper.CommentMapper;
import com.chotchip.task.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskService taskService;
    private final UserService userService;
//    private final CommentMapper commentMapper;

    @Transactional
    public CommentResponseDTO save(CommentCreateRequestDTO createRequestDTO, Authentication authentication) {
        Task task = taskService.getTask(createRequestDTO.getTaskId());
        User userByEmail = userService.getUserByEmail((String) authentication.getPrincipal());

        Comment save = commentRepository.save(new Comment(null, createRequestDTO.getDetails(), task, userByEmail));
        return new CommentResponseDTO(save.getDetails(), new UserResponseTaskDTO(save.getAuthor().getEmail()));
    }

}
