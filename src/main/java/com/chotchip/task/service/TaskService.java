package com.chotchip.task.service;

import com.chotchip.task.dto.request.CommentCreateInTaskRequestDTO;
import com.chotchip.task.dto.request.TaskCreateRequestDTO;
import com.chotchip.task.dto.request.TaskUpdateRequestDTO;
import com.chotchip.task.dto.request.TaskUpdateStatusRequestDTO;
import com.chotchip.task.dto.request.UserEmailRequestDTO;
import com.chotchip.task.dto.response.TaskResponseDTO;
import com.chotchip.task.entity.Comment;
import com.chotchip.task.entity.Task;
import com.chotchip.task.entity.User;
import com.chotchip.task.execption.UserNotRightsException;
import com.chotchip.task.mapper.TaskMapper;
import com.chotchip.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final TaskMapper taskMapper;

    public Page<TaskResponseDTO> getTaskByUser(UserEmailRequestDTO emailRequestDTO, PageRequest page) {
        User userByEmail = userService.getUserByEmail(emailRequestDTO.getEmail());
        Page<Task> byAuthorOrExecutor = taskRepository.findByExecutor(userByEmail, page);
        return byAuthorOrExecutor.map(taskMapper::toDTO);
    }

    public TaskResponseDTO getTaskById(Long id, Authentication authentication) {
        log.info("User %s request task %d".formatted(authentication.getPrincipal(), id));
        Task task = getTask(id);

        checkedUserUtil(authentication, task);

        return taskMapper.toDTO(task);
    }


    @Transactional
    public TaskResponseDTO save(TaskCreateRequestDTO requestDTO, Authentication authentication) {
        String authorEmail = (String) authentication.getPrincipal();
        log.info("User %s created task ".formatted(authorEmail));
        Task entity = taskMapper.toEntity(requestDTO);
        List<CommentCreateInTaskRequestDTO> commentRequest = requestDTO.getComment();
        List<Comment> comments = new ArrayList<>(commentRequest.size());

        User executor = userService.getUserByEmail(requestDTO.getExecutor());
        User author = userService.getUserByEmail(authorEmail);
        for (CommentCreateInTaskRequestDTO commentCreateInTaskRequestDTO : commentRequest) {
            Comment newComment = new Comment(null, commentCreateInTaskRequestDTO.getDetails(), entity, author);
            comments.add(newComment);
        }
        entity.setExecutor(executor);
        entity.setAuthor(author);
        entity.setComment(comments);
        Task save = taskRepository.save(entity);
        log.info("Task is save:");
        return taskMapper.toDTO(save);
    }

    @Transactional
    public TaskResponseDTO updateTask(Long id, TaskUpdateRequestDTO updateRequestDTO) {
        log.info("Admin updated task " + updateRequestDTO);
        Task task = getTask(id);
        task.setTitle(updateRequestDTO.getTitle());
        task.setDetails(updateRequestDTO.getDetails());
        task.setStatus(updateRequestDTO.getStatus());
        task.setPriority(updateRequestDTO.getPriority());
        log.info("Task successful updated " + task);
        return taskMapper.toDTO(task);
    }

    @Transactional
    public TaskResponseDTO patchStatusTask(Long id, TaskUpdateStatusRequestDTO status, Authentication authentication) {
        log.info("User %s updated status: " + status);
        Task task = getTask(id);
        checkedUserUtil(authentication, task);
        task.setStatus(status.getStatus());
        log.info("User %s successful updated status: " + status);
        return taskMapper.toDTO(task);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Admin delete task which id: %d".formatted(id));
        taskRepository.deleteById(id);
    }

    public Task getTask(Long id) {
        return taskRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    // Проверяет пользователя ли эта задача
    public static void checkedUserUtil(Authentication authentication, Task task) {
        if (!authentication.getAuthorities().stream().findFirst().get().getAuthority().equals("ADMIN")) {
            String userEmail = task.getExecutor().getEmail();
            boolean youTasks = userEmail.equals((String) authentication.getPrincipal());
            if (!youTasks) {
                log.warn("User %s try get access not to his task ".formatted(userEmail));
                throw new UserNotRightsException();
            }
        }
    }
}
