package com.jygoh.jyso.domain.comment.entity;

import com.jygoh.jyso.domain.board.entity.Board;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private String writer;

    @ColumnDefault("false")
    private Boolean isEdited;

    @ColumnDefault("false")
    private Boolean isDeleted;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public Comment(Long id, String content, String writer, Board board, Boolean isEdited, Boolean isDeleted, Comment parent, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.writer = writer;
        this.isEdited = isEdited;
        this.isDeleted = isDeleted;
        this.parent = parent;
        this.board = board;
        this.createdAt = createdAt;
    }
}
