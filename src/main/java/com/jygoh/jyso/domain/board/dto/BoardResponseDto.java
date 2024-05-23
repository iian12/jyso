package com.jygoh.jyso.domain.board.dto;

import com.jygoh.jyso.domain.board.entity.Category;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

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
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @Builder
    public BoardResponseDto(Long id, String title, String content, Category category, String writer, Integer viewCount, Integer likeCount, Integer commentCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.writer = writer;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
