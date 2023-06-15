package com.fly.robot.dao;

import com.fly.robot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);

    User findByPhone(String phoneNumber);

    User findByEmail(String email);

}
