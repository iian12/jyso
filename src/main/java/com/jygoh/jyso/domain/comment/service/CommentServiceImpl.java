package com.jygoh.jyso.domain.comment.service;

import com.jygoh.jyso.domain.board.entity.Board;
import com.jygoh.jyso.domain.board.repository.BoardRepository;
import com.jygoh.jyso.domain.board.service.BoardService;
import com.jygoh.jyso.domain.comment.dto.CommentCreateRequestDto;
import com.jygoh.jyso.domain.comment.dto.CommentResponseDto;
import com.jygoh.jyso.domain.comment.dto.CommentUpdateRequestDto;
import com.jygoh.jyso.domain.comment.entity.Comment;
import com.jygoh.jyso.domain.comment.repository.CommentRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final BoardService boardService;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public CommentServiceImpl(BoardService boardService, CommentRepository commentRepository, BoardRepository boardRepository) {
        this.boardService = boardService;
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
                .board(board)
                .isEdited(false)
                .isDeleted(false)
                .parent(parentComment)
                .createdAt(LocalDateTime.now())
                .build();

        commentRepository.save(comment);

        boardService.incrementCommentCount(board.getId());
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
}
