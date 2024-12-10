package com.chotchip.task.service;

import com.chotchip.task.dto.request.CommentCreateInTaskRequestDTO;
import com.chotchip.task.dto.request.TaskCreateRequestDTO;
import com.chotchip.task.dto.request.TaskUpdateRequestDTO;
import com.chotchip.task.dto.request.TaskUpdateStatusRequestDTO;
import com.chotchip.task.dto.response.CommentResponseDTO;
import com.chotchip.task.dto.response.TaskResponseDTO;
import com.chotchip.task.dto.response.UserResponseTaskDTO;
import com.chotchip.task.entity.Comment;
import com.chotchip.task.entity.Task;
import com.chotchip.task.entity.User;
import com.chotchip.task.entity.enums.Priority;
import com.chotchip.task.entity.enums.Status;
import com.chotchip.task.mapper.TaskMapper;
import com.chotchip.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final TaskMapper taskMapper;

    public Page<TaskResponseDTO> getTaskByUser(User user, PageRequest page) {

        Page<Task> byAuthorOrExecutor = taskRepository.findByAuthorOrExecutor(user, user, page);
        return byAuthorOrExecutor.map(taskMapper::toDTO);
    }

    public TaskResponseDTO getTaskById(Long id, Authentication authentication) {
        Task task = getTask(id);

        checkedUserUtil(authentication, task);

        return taskMapper.toDTO(task);
    }


    //:TODO добавить mapper ы
    @Transactional
    public TaskResponseDTO save(TaskCreateRequestDTO requestDTO, String authorEmail) {
        Task entity = new Task();
        entity.setTitle(requestDTO.getTitle());
        entity.setDetails(requestDTO.getDetails());
        entity.setStatus(Status.PENDING);
        entity.setPriority(Priority.LOW);

        List<CommentCreateInTaskRequestDTO> comment = requestDTO.getComment();
        List<Comment> comments = new ArrayList<>(comment.size());

        entity.setExecutor(userService.getUserByEmail(requestDTO.getExecutor()));
        User author = userService.getUserByEmail(authorEmail);
        entity.setAuthor(author);

        for (CommentCreateInTaskRequestDTO commentCreateInTaskRequestDTO : comment) {
            Comment newComment = new Comment(null, commentCreateInTaskRequestDTO.getDetails(), entity, author);
            comments.add(newComment);
        }

        entity.setComment(comments);
        Task save = taskRepository.save(entity);

        List<Comment> comment1 = save.getComment();
        List<CommentResponseDTO> commentResponseDTOS =
                comment1.stream()
                        .map(c -> new
                                CommentResponseDTO(c.getDetails(), new UserResponseTaskDTO(author.getEmail()))).toList();
        TaskResponseDTO taskResponseDTO =
                new TaskResponseDTO(save.getId(), save.getTitle(), save.getDetails(), save.getStatus(), save.getPriority()
                        , commentResponseDTOS, new UserResponseTaskDTO(save.getAuthor().getEmail()), new UserResponseTaskDTO(save.getExecutor().getEmail()));

        return taskResponseDTO;
    }

    @Transactional
    public TaskResponseDTO updateTask(Long id, TaskUpdateRequestDTO updateRequestDTO) {
        Task task = getTask(id);
        task.setTitle(updateRequestDTO.getTitle());
        task.setDetails(updateRequestDTO.getDetails());
        task.setStatus(updateRequestDTO.getStatus());
        task.setPriority(updateRequestDTO.getPriority());

        return taskMapper.toDTO(task);
    }

    @Transactional
    public TaskResponseDTO patchStatusTask(Long id, TaskUpdateStatusRequestDTO status, Authentication authentication) {
        Task task = getTask(id);
        checkedUserUtil(authentication, task);
        task.setStatus(status.getStatus());
        return taskMapper.toDTO(task);
    }

    public Task getTask(Long id) {
        return taskRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public static void checkedUserUtil(Authentication authentication, Task task) {
        if (!authentication.getAuthorities().stream().findFirst().get().getAuthority().equals("ADMIN")) {
            boolean youTasks = task.getExecutor().getEmail().equals((String) authentication.getPrincipal());
            if (!youTasks) {
                throw new RuntimeException("tasks not for you");
            }
        }
    }
}
