package com.jygoh.jyso.domain.comment.service;

import com.jygoh.jyso.domain.board.entity.Board;
import com.jygoh.jyso.domain.board.repository.BoardRepository;
import com.jygoh.jyso.domain.comment.dto.CommentCreateRequestDto;
import com.jygoh.jyso.domain.comment.dto.CommentResponseDto;
import com.jygoh.jyso.domain.comment.dto.CommentUpdateRequestDto;
import com.jygoh.jyso.domain.comment.entity.Comment;
import com.jygoh.jyso.domain.comment.repository.CommentRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public CommentServiceImpl(CommentRepository commentRepository, BoardRepository boardRepository) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
    }

    public void createComment(Long boardId, CommentCreateRequestDto requestDto, String nickname) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        Comment parentComment = null;
        if (requestDto.getParentId() != null) {
            parentComment = commentRepository.findById(requestDto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
        }

        Comment comment = Comment.builder()
                .content(requestDto.getContent())
                .writer(nickname)
                .parent(parentComment)
                .isEdited(false)
                .isDeleted(false)
                .build();

        commentRepository.save(comment);
    }

    public void updateComment(Long commentId, CommentUpdateRequestDto requestDto, String nickname) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        if (!comment.getWriter().equals(nickname)) {
            throw new AccessDeniedException("You don't have permission to update this comment");
        }

        Comment updatedComment = Comment.builder()
                .id(comment.getId())
                .content(requestDto.getContent())
                .writer(comment.getWriter())
                .board(comment.getBoard())
                .parent(comment.getParent())
                .isEdited(true)
                .isDeleted(comment.getIsDeleted())
                .createdAt(comment.getCreatedAt())
                .build();

        commentRepository.save(updatedComment);
    }

    public void deleteComment(Long commentId, String nickname) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        if (!comment.getWriter().equals(nickname)) {
            throw new AccessDeniedException("작성자만 댓글을 삭제할 수 있습니다.");
        }

        Comment deletedComment = Comment.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .writer(comment.getWriter())
                .board(comment.getBoard())
                .parent(comment.getParent())
                .isEdited(comment.getIsEdited())
                .isDeleted(true)
                .createdAt(comment.getCreatedAt())
                .build();

        commentRepository.save(deletedComment);
    }

    public List<CommentResponseDto> getCommentsByBoard(Long boardId) {
        List<Comment> comments = commentRepository.findByBoardIdAndParentIsNull(boardId);

        return comments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CommentResponseDto convertToDto(Comment comment) {
        List<CommentResponseDto> children = comment.getChildren().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .writer(comment.getWriter())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .isEdited(comment.getIsEdited())
                .isDeleted(comment.getIsDeleted())
                .createdAt(comment.getCreatedAt())
                .children(children)
                .build();
    }
}
