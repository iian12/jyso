package com.jygoh.jyso.domain.board.entity;

import com.jygoh.jyso.domain.comment.entity.Comment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String writer;

    @ColumnDefault("0")
    private Integer viewCount;

    @ColumnDefault("0")
    private Integer likeCount;

    @ColumnDefault("0")
    private Integer commentCount;

    @ColumnDefault("false")
    private Boolean isEdited;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Board(Long id, String title, String content, Category category, String writer, Integer viewCount, Integer likeCount, Integer commentCount, Boolean isEdited, LocalDateTime createdAt, LocalDateTime updatedAt) {
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
    }
}
