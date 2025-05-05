package com.webkorps.friendBook.repository;

import com.webkorps.friendBook.model.Follower;
import com.webkorps.friendBook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {
    Optional<Follower> findByFollowerAndFollowing(User follower, User following);

    List<Follower> findByFollowing(User following);
    List<Follower> findByFollower(User follower);

    Long countByFollowing(User user);
    Long countByFollower(User user);
}
