package com.jygoh.jyso.domain.board.dto;

import com.jygoh.jyso.domain.board.entity.Category;
import com.jygoh.jyso.domain.comment.dto.CommentResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardResponseDto {

    private final Long id;
    private final String title;
    private final String content;
    private final Category category;
    private final String writer;
    private final Integer viewCount;
    private final Integer likeCount;
    private final Integer commentCount;
    private final Boolean isEdited;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final List<CommentResponseDto> comments;

    @Builder
    public BoardResponseDto(Long id, String title, String content, Category category, String writer, Integer viewCount, Integer likeCount, Integer commentCount, Boolean isEdited, LocalDateTime createdAt, LocalDateTime updatedAt, List<CommentResponseDto> comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.writer = writer;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.isEdited = isEdited;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.comments = comments;
    }
}
