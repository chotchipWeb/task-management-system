package com.chotchip.task.contoller;

import com.chotchip.task.dto.request.CommentCreateRequestDTO;
import com.chotchip.task.dto.response.CommentResponseDTO;
import com.chotchip.task.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments/")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDTO> createComment(@RequestBody CommentCreateRequestDTO createRequestDTO, Authentication authentication) {
        return ResponseEntity.ok(commentService.save(createRequestDTO,authentication));
    }
}
