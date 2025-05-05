package com.webkorps.friendBook.repository;

import com.webkorps.friendBook.model.Like;
import com.webkorps.friendBook.model.Post;
import com.webkorps.friendBook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserAndPost(User user, Post post);

    long countByPostId(Long postId);

    void deleteByUserAndPost(User user, Post post);
}
