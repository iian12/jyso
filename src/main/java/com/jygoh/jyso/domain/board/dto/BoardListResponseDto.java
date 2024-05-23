package com.jygoh.jyso.domain.board.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardListResponseDto {

    private final Long id;
    private final String title;
    private final String writer;
    private final Integer likeCount;
    private final Integer viewCount;
    private final Integer commentCount;
    private final LocalDateTime createdAt;

    @Builder
    public BoardListResponseDto(Long id, String title, String writer, Integer likeCount, Integer viewCount, Integer commentCount, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
    }
}
