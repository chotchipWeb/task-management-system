package com.chotchip.task.contoller;

import com.chotchip.task.entity.Task;
import com.chotchip.task.entity.User;
import com.chotchip.task.service.TaskService;
import com.chotchip.task.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
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
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<Task>> getTasks(
            @RequestParam int page,
            @RequestParam int size,
            Authentication authentication
    ) {

        String email = (String) authentication.getPrincipal();
        User user = userService.getUserByEmail(email);

        return ResponseEntity.ok(taskService.getTaskByUser(user, PageRequest.of(page, size)));

    }

}
