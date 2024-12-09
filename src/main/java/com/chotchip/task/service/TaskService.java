package com.chotchip.task.service;

import com.chotchip.task.entity.Task;
import com.chotchip.task.entity.User;
import com.chotchip.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public Page<Task> getTaskByUser(User user, PageRequest page) {
        return taskRepository.findByAuthorOrExecutor(user,user,page);
    }
}
