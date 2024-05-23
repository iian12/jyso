package com.jygoh.jyso.domain.comment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentCreateRequestDto {

    private final String content;
    private final Long parentId;

    @Builder
    public CommentCreateRequestDto(String content, Long parentId) {
        this.content = content;
        this.parentId = parentId;
    }
}
