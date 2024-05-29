package com.jygoh.jyso.domain.comment.controller;

import com.jygoh.jyso.domain.comment.dto.CommentCreateRequestDto;
import com.jygoh.jyso.domain.comment.dto.CommentUpdateRequestDto;
import com.jygoh.jyso.domain.comment.service.CommentService;
import com.jygoh.jyso.domain.member.service.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{boardId}/comments")
    public ResponseEntity<?> createComment(@PathVariable Long boardId,
                                           @RequestBody CommentCreateRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String nickname = userDetails.getNickname();
        commentService.createComment(boardId, requestDto, nickname);
        return ResponseEntity.ok().body("success");
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable Long commentId,
                                              @RequestBody CommentUpdateRequestDto requestDto,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String nickname = userDetails.getNickname();
        commentService.updateComment(commentId, requestDto, nickname);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String nickname = userDetails.getNickname();
        commentService.deleteComment(commentId, nickname);
        return ResponseEntity.ok().build();
    }
}
