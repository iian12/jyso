package com.jygoh.jyso.domain.heart.repository;

import com.jygoh.jyso.domain.heart.entity.CommentHeart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentHeartRepository extends JpaRepository<CommentHeart, Long> {
}
