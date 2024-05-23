package com.jygoh.jyso.domain.comment.repository;

import com.jygoh.jyso.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
