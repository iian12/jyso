package com.jygoh.jyso.domain.board.controller;

import com.jygoh.jyso.domain.board.dto.BoardCreateRequestDto;
import com.jygoh.jyso.domain.board.dto.BoardListResponseDto;
import com.jygoh.jyso.domain.board.dto.BoardResponseDto;
import com.jygoh.jyso.domain.board.dto.BoardUpdateRequestDto;
import com.jygoh.jyso.domain.board.entity.Category;
import com.jygoh.jyso.domain.board.service.BoardService;
import com.jygoh.jyso.domain.member.service.UserDetailsImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    public ResponseEntity<String> createBoard(@RequestBody BoardCreateRequestDto request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String nickname = userDetails.getNickname();

        boardService.createBoard(request, nickname);

        return ResponseEntity.status(HttpStatus.CREATED).body("Success");
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<String> updateBoard(@PathVariable Long boardId, @RequestBody BoardUpdateRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String nickname = userDetails.getNickname();
        boardService.updateBoard(boardId, requestDto, nickname);
        return ResponseEntity.status(HttpStatus.OK).body("게시글이 성공적으로 수정되었습니다.");
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String nickname = userDetails.getNickname();
        boardService.deleteBoard(boardId, nickname);

        return ResponseEntity.status(HttpStatus.OK).body("게시글이 성공적으로 삭제되었습니다.");
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<BoardListResponseDto>> getBoardList(@PathVariable Category category) {
        List<BoardListResponseDto> boardResponses = boardService.getBoardByCategory(category);
        return ResponseEntity.ok(boardResponses);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> getBoard(@PathVariable Long boardId) {
        BoardResponseDto boardResponse = boardService.getBoard(boardId);
        return ResponseEntity.ok(boardResponse);
    }
}
