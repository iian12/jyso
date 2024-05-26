package com.jygoh.jyso.domain.board.service;

import com.jygoh.jyso.domain.board.dto.BoardCreateRequestDto;
import com.jygoh.jyso.domain.board.dto.BoardListResponseDto;
import com.jygoh.jyso.domain.board.dto.BoardResponseDto;
import com.jygoh.jyso.domain.board.dto.BoardUpdateRequestDto;
import com.jygoh.jyso.domain.board.entity.Category;

import java.util.List;

public interface BoardService {

    void createBoard(BoardCreateRequestDto requestDto, String nickname);

    void updateBoard(Long boardId, BoardUpdateRequestDto requestDto, String nickname);

    void deleteBoard(Long boardId, String nickname);

    BoardResponseDto getBoard(Long boardId);

    List<BoardListResponseDto> getBoardByCategory(Category category, int page, int size);

    void incrementCommentCount(Long boardId);
}
