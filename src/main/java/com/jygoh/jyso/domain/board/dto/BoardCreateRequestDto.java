package com.jygoh.jyso.domain.board.dto;

import com.jygoh.jyso.domain.board.entity.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardCreateRequestDto {

    private String title;

    private String content;

    private Category category;

    @Builder
    public BoardCreateRequestDto(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }
}
