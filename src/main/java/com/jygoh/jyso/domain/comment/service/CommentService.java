package com.jygoh.jyso.domain.comment.service;

import com.jygoh.jyso.domain.comment.dto.CommentCreateRequestDto;
import com.jygoh.jyso.domain.comment.dto.CommentResponseDto;
import com.jygoh.jyso.domain.comment.dto.CommentUpdateRequestDto;

import java.util.List;

public interface CommentService {

    void createComment(Long boardId, CommentCreateRequestDto requestDto, String nickname);
    void updateComment(Long commentId, CommentUpdateRequestDto requestDto, String nickname);
    void deleteComment(Long commentId, String nickname);
}
