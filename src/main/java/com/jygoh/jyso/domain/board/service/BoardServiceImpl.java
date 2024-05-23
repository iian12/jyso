package com.jygoh.jyso.domain.board.service;

import com.jygoh.jyso.domain.board.dto.BoardCreateRequestDto;
import com.jygoh.jyso.domain.board.dto.BoardListResponseDto;
import com.jygoh.jyso.domain.board.dto.BoardResponseDto;
import com.jygoh.jyso.domain.board.dto.BoardUpdateRequestDto;
import com.jygoh.jyso.domain.board.entity.Board;
import com.jygoh.jyso.domain.board.entity.Category;
import com.jygoh.jyso.domain.board.repository.BoardRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public void createBoard(BoardCreateRequestDto requestDto, String nickname) {

        Board board = Board.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .category(requestDto.getCategory())
                .writer(nickname)
                .viewCount(0)
                .likeCount(0)
                .commentCount(0)
                .build();

        boardRepository.save(board);
    }

    public void updateBoard(Long boardId, BoardUpdateRequestDto requestDto, String nickname) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        if (!board.getWriter().equals(nickname)) {
            throw new AccessDeniedException("작성자만 게시글을 수정할 수 있음");
        }

        Board updateBoard = Board.builder()
                .id(board.getId())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .category(board.getCategory())
                .writer(board.getWriter())  // 기존 writer 유지
                .viewCount(board.getViewCount())  // 기존 viewCount 유지
                .likeCount(board.getLikeCount())  // 기존 likeCount 유지
                .commentCount(board.getCommentCount())  // 기존 commentCount 유지
                .createdAt(board.getCreatedAt())  // 기존 createdAt 유지
                .updatedAt(LocalDateTime.now())
                .build();

        boardRepository.save(updateBoard);
    }

    @Override
    public void deleteBoard(Long boardId, String nickname) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        if (!board.getWriter().equals(nickname)) {
            throw new AccessDeniedException("작성자만 게시글을 삭제할 수 있음.");
        }

        boardRepository.delete(board);
    }

    public BoardResponseDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        return BoardResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .category(board.getCategory())
                .writer(board.getWriter())
                .viewCount(board.getViewCount())
                .likeCount(board.getLikeCount())
                .commentCount(board.getCommentCount())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }

    public List<BoardListResponseDto> getBoardByCategory(Category category) {
        return boardRepository.findByCategory(category).stream()
                .map(board -> BoardListResponseDto.builder()
                        .id(board.getId())
                        .title(board.getTitle())
                        .writer(board.getWriter())
                        .likeCount(board.getLikeCount())
                        .viewCount(board.getViewCount())
                        .commentCount(board.getCommentCount())
                        .createdAt(board.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
