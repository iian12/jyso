package com.jygoh.jyso.domain.comment.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CommentResponseDto {
    private final Long id;
    private final String content;
    private final String writer;
    private final Long parentId;
    private final Boolean isEdited;
    private final Boolean isDeleted;
    private final LocalDateTime createdAt;
    private final List<CommentResponseDto> children;

    @Builder
    public CommentResponseDto(Long id, String content, String writer, Long parentId, Boolean isEdited, Boolean isDeleted, LocalDateTime createdAt, List<CommentResponseDto> children) {
        this.id = id;
        this.content = content;
        this.writer = writer;
        this.parentId = parentId;
        this.isEdited = isEdited;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.children = children;
    }
}
