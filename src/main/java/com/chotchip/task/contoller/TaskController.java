package com.chotchip.task.contoller;

import com.chotchip.task.dto.request.TaskCreateRequestDTO;
import com.chotchip.task.dto.request.TaskUpdateRequestDTO;
import com.chotchip.task.dto.request.TaskUpdateStatusRequestDTO;
import com.chotchip.task.dto.response.TaskResponseDTO;
import com.chotchip.task.entity.Task;
import com.chotchip.task.entity.User;
import com.chotchip.task.entity.enums.Status;
import com.chotchip.task.service.TaskService;
import com.chotchip.task.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<TaskResponseDTO>> getTasks(
            @RequestParam int page,
            @RequestParam int size,
            Authentication authentication
    ) {

        String email = (String) authentication.getPrincipal();
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(taskService.getTaskByUser(user, PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity
                .ok(taskService.getTaskById(id, authentication));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskCreateRequestDTO taskCreateRequestDTO, Authentication authentication, UriComponentsBuilder uriBuilder) {
        TaskResponseDTO task = taskService.save(taskCreateRequestDTO, (String) authentication.getPrincipal());
        return ResponseEntity
                .created(uriBuilder
                        .replacePath("/api/tasks/{id}")
                        .build(Map.of("id", task.getId())))
                .body(task);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @RequestBody TaskUpdateRequestDTO updateRequestDTO) {
        return ResponseEntity.ok(taskService.updateTask(id, updateRequestDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> patchTask(@PathVariable Long id, @RequestBody TaskUpdateStatusRequestDTO status, Authentication authentication) {
        return ResponseEntity.ok(taskService.patchStatusTask(id,status,authentication));
    }

}
