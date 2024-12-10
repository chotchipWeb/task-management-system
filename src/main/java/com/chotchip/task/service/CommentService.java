package com.chotchip.task.service;

import com.chotchip.task.dto.request.CommentCreateInTaskRequestDTO;
import com.chotchip.task.entity.Comment;
import com.chotchip.task.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public void save(Comment comment) {

    }

}
