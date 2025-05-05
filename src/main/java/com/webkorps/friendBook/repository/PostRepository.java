package com.webkorps.friendBook.repository;

import com.webkorps.friendBook.model.Post;
import com.webkorps.friendBook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {
    Optional<List<Post>> findByUser(User user);
}
