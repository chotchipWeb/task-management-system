package com.chotchip.task.service;

import com.chotchip.task.dto.request.CommentCreateInTaskRequestDTO;
import com.chotchip.task.dto.request.TaskCreateRequestDTO;
import com.chotchip.task.dto.request.TaskUpdateRequestDTO;
import com.chotchip.task.dto.request.TaskUpdateStatusRequestDTO;
import com.chotchip.task.dto.request.UserEmailRequestDTO;
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
import com.chotchip.task.execption.UserNotRightsException;
import com.chotchip.task.mapper.TaskMapper;
import com.chotchip.task.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    @DisplayName("Проверка подлинности задачи на соответствие его исполнителя/админа, запрос от админа, успешен")
    void checkedUserUtil_RequestAdminTask_ResponseVoid() {
        //given
        Task task = createTask();
        User auhthorAdminUser = createAdminUser();
        User executorAdminUser = createAdminUser();
        task.setAuthor(auhthorAdminUser);
        task.setExecutor(executorAdminUser);
        UsernamePasswordAuthenticationToken authentication = createAuthentication(executorAdminUser);
        //then
        TaskService.checkedUserUtil(authentication, task);
    }

    @Test
    @DisplayName("Проверка подлинности задачи на соответствие его исполнителя/админа, запрос от пользователя который выполняет эту задачу, успешен")
    void checkedUserUtil_RequestClientTask_ResponseVoid() {
        //given
        Task task = createTask();
        User auhthorAdminUser = createAdminUser();
        User executorAdminUser = createClientUser();
        task.setAuthor(auhthorAdminUser);
        task.setExecutor(executorAdminUser);

        UsernamePasswordAuthenticationToken authentication = createAuthentication(executorAdminUser);
        //then
        TaskService.checkedUserUtil(authentication, task);
    }

    @Test
    @DisplayName("Проверка подлинности задачи на соответствие его исполнителя/админа, " +
            "запрос от пользователя у которого не эта задача, ошибка прав доступа")
    void checkedUserUtil_RequestClientNotAccess_ResponseUserNotRightsException() {
        //given
        Task task = createTask();
        User auhthorAdminUser = createAdminUser();
        task.setAuthor(auhthorAdminUser);
        task.setExecutor(auhthorAdminUser);

        User userNoAccessToTask = createClientUser();

        UsernamePasswordAuthenticationToken authentication = createAuthentication(userNoAccessToTask);
        Object principal = authentication.getPrincipal();
        System.out.println(principal);
        //then
        UserNotRightsException exception =
                assertThrows(UserNotRightsException.class, () -> TaskService.checkedUserUtil(authentication, task));
        assertEquals("User not rights to this task", exception.getMessage());
    }

    @Test
    void getTask_RequestIdIsValid_ResponseTask() {
        // given
        long idTask = 1L;
        String title = "title";
        String details = "details";
        Status status = Status.IN_PROGRESS;
        Priority priority = Priority.MIDDLE;
        List<Comment> comment = List.of();
        Optional<Task> taskByRepo = Optional.of(createTask());
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
        //then
        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () ->
                this.taskService.getTask(idTaskNotExistent));
        assertEquals("Task which id: %s not found".formatted(idTaskNotExistent), exception.getMessage());

    }

    @Test
    void getTaskByUser_RequestIsValidEmailAndPage_Response() {
        // given
        User clientUser = createClientUser();

        doReturn(clientUser)
                .when(this.userService)
                .getUserByEmail(clientUser.getEmail());
        Task task = createTask();
        PageRequest pageable = PageRequest.of(0, 1);
        Page<Task> tasks = new PageImpl<>(List.of(task), pageable, 1);

        doReturn(tasks)
                .when(this.taskRepository)
                .findByExecutor(clientUser, pageable);
        TaskResponseDTO taskToDTO = new TaskResponseDTO(task.getId(), task.getTitle(), task.getDetails(), task.getStatus(), task.getPriority(),
                List.of(), null, null);
        doReturn(taskToDTO)
                .when(this.taskMapper)
                .toDTO(task);
        // when
        Page<TaskResponseDTO> taskByUser = this.taskService.getTaskByUser(new UserEmailRequestDTO(clientUser.getEmail()), pageable);
        // then
        TaskResponseDTO assertTaskResponse = new TaskResponseDTO(task.getId(), task.getTitle(), task.getDetails(), task.getStatus(), task.getPriority(),
                List.of(), null, null);
        List<TaskResponseDTO> taskResponseDTOS = List.of(assertTaskResponse);
        Page<TaskResponseDTO> assertPage = new PageImpl<>(taskResponseDTOS, Pageable.ofSize(1), 1);
        assertEquals(assertPage, taskByUser);
    }

    @Test
    void getTaskById__RequestIsValid_ResponseDTO() {
        // given

        // Receipt task from repository (Получение задачи из репозитория)
        User authorUser = createAdminUser();
        String email = authorUser.getEmail();
        Optional<Task> taskFromRepo = Optional.of(createTask());
        Long idTask = taskFromRepo.get().getId();
        doReturn(taskFromRepo)
                .when(this.taskRepository)
                .findById(idTask);
        Task task = this.taskService.getTask(idTask);
        authorUser.getTasks().add(task);

        // Response DTO from mapper
        UserResponseTaskDTO userAuthorDTO = new UserResponseTaskDTO(email);
        UserResponseTaskDTO userExecutorDTO = new UserResponseTaskDTO(email);
        List<CommentResponseDTO> commentDTO = List.of();
        TaskResponseDTO taskResponseDTO =
                new TaskResponseDTO(idTask, task.getTitle(), task.getDetails(), task.getStatus(), task.getPriority(),
                        commentDTO, userAuthorDTO, userExecutorDTO);

        doReturn(taskResponseDTO)
                .when(this.taskMapper)
                .toDTO(task);

        // when
        TaskResponseDTO result = this.taskService.getTaskById(idTask, createAuthentication(authorUser));

        TaskResponseDTO checkingResult = new TaskResponseDTO(idTask, task.getTitle(), task.getDetails(), task.getStatus(), task.getPriority(),
                commentDTO, userAuthorDTO, userExecutorDTO);
        //then
        assertEquals(result, checkingResult);
    }

    @Test
    void save_RequestIsValid_ResponseDTO() {
        //given
        User adminUser = createAdminUser();
        User clientUser = createClientUser();
        long id = 1L;
        String title = "title";
        String details = "details";
        Priority priority = Priority.LOW;
        Status status = Status.PENDING;
        List<CommentCreateInTaskRequestDTO> commentCreate = List.of();
        TaskCreateRequestDTO requestDTO = new TaskCreateRequestDTO(title, details, priority, commentCreate, clientUser.getEmail());

        Task task = new Task(id, title, details, status, priority, List.of(), null, null);
        List<CommentResponseDTO> commentResponseDTO = List.of();
        TaskResponseDTO taskResponseDTO = new TaskResponseDTO(id, title, details, status, priority, commentResponseDTO, new UserResponseTaskDTO(adminUser.getEmail()), new UserResponseTaskDTO(clientUser.getEmail()));
        doReturn(task)
                .when(this.taskMapper)
                .toEntity(requestDTO);
        doReturn(adminUser)
                .when(this.userService)
                .getUserByEmail(adminUser.getEmail());
        doReturn(clientUser)
                .when(this.userService)
                .getUserByEmail(clientUser.getEmail());
        doReturn(task)
                .when(this.taskRepository)
                .save(task);
        task.setAuthor(adminUser);
        task.setExecutor(clientUser);
        doReturn(taskResponseDTO)
                .when(this.taskMapper)
                .toDTO(task);
        UsernamePasswordAuthenticationToken authentication = createAuthentication(adminUser);

        //when
        TaskResponseDTO save = taskService.save(requestDTO, authentication);
        //then
        TaskResponseDTO assertTask = new TaskResponseDTO(id, title, details, status, priority, commentResponseDTO, new UserResponseTaskDTO(adminUser.getEmail()), new UserResponseTaskDTO(clientUser.getEmail()));
        assertEquals(save, assertTask);
    }

    @Test
    void updateTask_RequestIsValid_ResponseUpdateDTO() {
        // given
        Task task = createTask();
        Long id = task.getId();
        TaskUpdateRequestDTO taskUpdateRequestDTO = new TaskUpdateRequestDTO(task.getTitle(), task.getDetails(), task.getStatus(), task.getPriority());
        doReturn(Optional.of(task))
                .when(this.taskRepository)
                .findById(id);
        String newTitle = "new Title";
        TaskResponseDTO createTaskResponseDTO = new TaskResponseDTO(task.getId(), newTitle, task.getDetails(), task.getStatus(), task.getPriority(),
                List.of(), null, null);
        doReturn(createTaskResponseDTO)
                .when(this.taskMapper)
                .toDTO(task);
        // when
        TaskResponseDTO taskResponseDTO = taskService.updateTask(id, taskUpdateRequestDTO);

        // then
        TaskResponseDTO assertTaskDTO = new TaskResponseDTO(task.getId(), newTitle, task.getDetails(), task.getStatus(), task.getPriority(),
                List.of(), null, null);
        assertEquals(assertTaskDTO, taskResponseDTO);
    }

    @Test
    void patchStatusTask_RequestIsValid_ResponseDTO() {
        Task task = createTask();
        User adminUser = createAdminUser();
        task.setAuthor(adminUser);
        task.setExecutor(adminUser);
        Long id = task.getId();
        doReturn(Optional.of(task))
                .when(this.taskRepository)
                .findById(id);

        UsernamePasswordAuthenticationToken authentication = createAuthentication(adminUser);
        TaskUpdateStatusRequestDTO taskUpdateRequestDTO = new TaskUpdateStatusRequestDTO(Status.COMPLETED);

        TaskResponseDTO createTaskResponseDTO = new TaskResponseDTO(task.getId(), task.getTitle(), task.getDetails(), taskUpdateRequestDTO.getStatus(), task.getPriority(),
                List.of(), null, null);
        doReturn(createTaskResponseDTO)
                .when(this.taskMapper)
                .toDTO(task);
        // when
        TaskResponseDTO taskResponseDTO = taskService.patchStatusTask(id, taskUpdateRequestDTO, authentication);

        // then
        TaskResponseDTO assertDTO = new TaskResponseDTO(task.getId(), task.getTitle(), task.getDetails(), taskUpdateRequestDTO.getStatus(), task.getPriority(),
                List.of(), null, null);

        assertEquals(assertDTO, taskResponseDTO);
    }

    private UsernamePasswordAuthenticationToken createAuthentication(User authorUser) {
        return new UsernamePasswordAuthenticationToken(authorUser.getEmail(), authorUser.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(String.valueOf(authorUser.getRole()))));
    }

    private Task createTask() {
        long idTask = 1L;
        String title = "title";
        String details = "details";
        Status status = Status.IN_PROGRESS;
        Priority priority = Priority.MIDDLE;
        List<Comment> comment = new ArrayList<>();
        return new Task(idTask, title, details, status, priority, comment, null, null);
    }

    private User createAdminUser() {
        long id = 1L;
        String emailAuthor = "admin@gmail.com";
        String password = "password";
        Role role = Role.ADMIN;
        List<Task> list = new ArrayList<>();
        return new User(id, emailAuthor, password, role, list);
    }

    private User createClientUser() {
        long id = 2L;
        String emailAuthor = "client@gmail.com";
        String password = "password";
        Role role = Role.CLIENT;
        List<Task> list = new ArrayList<>();
        return new User(id, emailAuthor, password, role, list);
    }

}