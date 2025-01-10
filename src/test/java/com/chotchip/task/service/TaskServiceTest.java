package com.chotchip.task.service;

import com.chotchip.task.dto.response.CommentResponseDTO;
import com.chotchip.task.dto.response.TaskResponseDTO;
import com.chotchip.task.dto.response.UserResponseTaskDTO;
import com.chotchip.task.entity.Comment;
import com.chotchip.task.entity.Task;
import com.chotchip.task.entity.User;
import com.chotchip.task.entity.enums.Priority;
import com.chotchip.task.entity.enums.Role;
import com.chotchip.task.entity.enums.Status;
import com.chotchip.task.execption.TaskNotFoundException;
import com.chotchip.task.mapper.TaskMapper;
import com.chotchip.task.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@Execution(ExecutionMode.CONCURRENT)
class TaskServiceTest {

    @Mock
    TaskRepository taskRepository;

    @Mock
    UserService userService;
    @Mock
    TaskMapper taskMapper;
    @InjectMocks
    TaskService taskService;



    @Test
    void getTask_RequestIdIsValid_ResponseTask() {
        // given
        long idTask = 1L;
        String title = "title";
        String details = "details";
        Status status = Status.IN_PROGRESS;
        Priority priority = Priority.MIDDLE;
        List<Comment> comment = List.of();
        Optional<Task> taskByRepo = Optional.of(new Task(idTask, title, details, status, priority, comment, null, null));
        doReturn(taskByRepo)
                .when(this.taskRepository)
                .findById(idTask);

        //when
        Task task = taskService.getTask(idTask);

        // then
        assertEquals(new Task(idTask, title, details, status, priority, comment, null, null), task);

    }

    @Test
    void getTask_RequestIdIsNotExistent_ResponseTaskNotFoundException() {
        // given;
        long idTaskNotExistent = 10000000L;
        Optional<Task> empty = Optional.empty();
        doReturn(empty)
                .when(this.taskRepository)
                .findById(idTaskNotExistent);
        //when
        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () ->
                this.taskService.getTask(idTaskNotExistent));
        // then
        assertEquals("Task which id: %s not found".formatted(idTaskNotExistent), exception.getMessage());

    }

    @Test
    void getTaskByUser_RequestIsValidEmailAndPage_Response() {
        // given
//        User userByEmail = new User(1L, "amdin@mail.com", "password", Role.CLIENT, List.of());
//        doReturn(new PageImpl<>(List.of(new Task()), Pageable.ofSize()))
//                .when(this.taskRepository)
//                .findByExecutor(userByEmail, PageRequest.of(0, 0));
        // when

    }

    @Test
    void getTaskById__RequestIsValid_ResponseDTO() {
        // given

        // Receipt task from repository (Получение задачи из репозитория)
        long idTask = 1L;
        String title = "title";
        String details = "details";
        Status status = Status.IN_PROGRESS;
        Priority priority = Priority.MIDDLE;
        List<Comment> comment = List.of();

        // User author and executor
        List<Task> authorTask = new ArrayList<>();
        String emailAuthor = "email@gmail.com";
        String emailExecutor = "email@gmail.com";
        Role role = Role.ADMIN;
        String password = "password";
        User author = new User(1L, emailAuthor, password, role, authorTask);
        User executor = new User(1L, emailExecutor, password, role, authorTask);


        Optional<Task> taskFromRepo = Optional.of(new Task(idTask, title, details, status,
                priority, comment, author, executor));

        doReturn(taskFromRepo)
                .when(this.taskRepository)
                .findById(idTask);
        Task task = this.taskService.getTask(idTask);
        authorTask.add(task);
        // Response DTO from mapper

        UserResponseTaskDTO userAuthorDTO = new UserResponseTaskDTO(emailAuthor);
        UserResponseTaskDTO userExecutorDTO = new UserResponseTaskDTO(emailExecutor);
        List<CommentResponseDTO> commentDTO = List.of();
        TaskResponseDTO taskResponseDTO =
                new TaskResponseDTO(idTask, title, details, status, priority,
                        commentDTO, userAuthorDTO, userExecutorDTO);

        doReturn(taskResponseDTO)
                .when(this.taskMapper)
                .toDTO(task);

        // when
        TaskResponseDTO result = this.taskService.getTaskById(idTask,
                new UsernamePasswordAuthenticationToken(emailAuthor, password,
                        Collections.singleton(new SimpleGrantedAuthority(String.valueOf(role)))));

        TaskResponseDTO checkingResult = new TaskResponseDTO(idTask, title, details, status, priority, commentDTO, userAuthorDTO, userExecutorDTO);
        //then
        assertEquals(result, checkingResult);
    }

    @Test
    void save() {

    }

    @Test
    void updateTask() {
    }

    @Test
    void patchStatusTask() {
    }

    
}