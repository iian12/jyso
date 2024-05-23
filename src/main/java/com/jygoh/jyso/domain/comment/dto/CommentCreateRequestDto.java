package com.jygoh.jyso.domain.comment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentCreateRequestDto {

    private final String content;
    private final Long parentId;

    @Builder
    public CommentCreateRequestDto(Long parentId, String content) {
        this.parentId = parentId;
        this.content = content;
    }
}
