package com.chotchip.task.repository;

import com.chotchip.task.entity.Task;
import com.chotchip.task.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByAuthorOrExecutor(User author, User executor, Pageable pageable);
}
