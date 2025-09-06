package com.example.mdswebsite.repository;

import com.example.mdswebsite.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> search(Long uid, Long teamId, String status, String tag, Long afterId, Pageable p);

    Optional<Todo> findByIdAndOwnerUserId(Long id, Long ownerUserId);
    long countByOwnerUserId(Long ownerUserId);
}
