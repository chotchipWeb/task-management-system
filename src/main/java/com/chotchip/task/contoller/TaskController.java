package com.chotchip.task.contoller;

import com.chotchip.task.dto.request.TaskCreateRequestDTO;
import com.chotchip.task.dto.request.TaskUpdateRequestDTO;
import com.chotchip.task.dto.request.TaskUpdateStatusRequestDTO;
import com.chotchip.task.dto.request.UserEmailRequestDTO;
import com.chotchip.task.dto.response.TaskResponseDTO;
import com.chotchip.task.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping("api/tasks/")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/email")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Получение список задач по почте исполнителя",
            description = "Доступна только с правами Администратора ",
            parameters = {
                    @Parameter(
                            name = "page",
                            description = "Номер страницы (начиная с 0)",
                            required = true,
                            schema = @Schema(type = "integer", example = "0")
                    ),
                    @Parameter(
                            name = "size",
                            description = "Размер страницы (количество элементов на странице)",
                            required = true,
                            schema = @Schema(type = "integer", example = "10")
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Тело запроса, содержащее email пользователя",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UserEmailRequestDTO.class)
                    )
            ))
    public ResponseEntity<Page<TaskResponseDTO>> getTasks(
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size,
            @RequestBody @Valid UserEmailRequestDTO userRequestEmail,
             BindingResult bindingResult
    ) throws BindException {
        if (bindingResult.hasErrors()) throw new BindException(bindingResult);
        else return ResponseEntity.ok(taskService.getTaskByUser(userRequestEmail, PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение задачи по id", description = "Доступна всем админам и тому пользователю чья это задача")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity
                .ok(taskService.getTaskById(id, authentication));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Создание задачи",
            description = "Доступна только с правами Администратора, должен быть хотя бы один пользователь, " +
                    "приоритет должен быть один из этого списка: HIGH, MIDDLE, LOW")
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody @Valid TaskCreateRequestDTO taskCreateRequestDTO,
                                                      Authentication authentication,
                                                      UriComponentsBuilder uriBuilder,
                                                      BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        } else {
            TaskResponseDTO task = taskService.save(taskCreateRequestDTO, authentication);
            return ResponseEntity
                    .created(uriBuilder
                            .replacePath("/api/tasks/{id}")
                            .build(Map.of("id", task.getId())))
                    .body(task);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Обновление задачи", description = "Доступна только с правами Администратора, можно поменять: название, описание, статус задачи(PENDING, IN_PROGRESS, COMPLETED), приоритет задачи(HIGH, MIDDLE, LOW)")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @RequestBody @Valid TaskUpdateRequestDTO updateRequestDTO,
                                                      BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) throw new BindException(bindingResult);
        else return ResponseEntity.ok(taskService.updateTask(id, updateRequestDTO));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Обновление статуса задачи", description = "Доступна только с правами Администратора, статус задачи(PENDING, IN_PROGRESS, COMPLETED)")
    public ResponseEntity<TaskResponseDTO> patchTask(@PathVariable Long id, @RequestBody @Valid TaskUpdateStatusRequestDTO status,
                                                     Authentication authentication,
                                                     BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) throw new BindException(bindingResult);
        else return ResponseEntity.ok(taskService.patchStatusTask(id, status, authentication));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Удаление задачи по id", description = "Доступна только с правами Администратора")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }

}
