package com.fly.robot.dao;

import com.fly.robot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDateTime;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);

    User findByPhone(String phoneNumber);

    User findByEmail(String email);

    @Transactional
    @Modifying
    @Query("update User u set u.lastLoginTime = :lastLoginTime where u.userId = :userId")
    void updateLastLoginTime(@Param("userId") String userId, @Param("lastLoginTime") LocalDateTime lastLoginTime);
}
