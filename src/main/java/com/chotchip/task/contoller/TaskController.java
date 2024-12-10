package com.chotchip.task.contoller;

import com.chotchip.task.dto.request.TaskCreateRequestDTO;
import com.chotchip.task.dto.response.TaskResponseDTO;
import com.chotchip.task.entity.Task;
import com.chotchip.task.entity.User;
import com.chotchip.task.service.TaskService;
import com.chotchip.task.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/tasks/")
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
   
    @PostMapping
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskCreateRequestDTO taskCreateRequestDTO, Authentication authentication) {
        TaskResponseDTO task = taskService.save(taskCreateRequestDTO, (String) authentication.getPrincipal());
        return ResponseEntity
                .ok(task);
    }

}
