package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Session;
import com.example.demo.entity.User;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByUser(User user);

    Optional<Session> findByRefreshToken(String refreshToken);
}
