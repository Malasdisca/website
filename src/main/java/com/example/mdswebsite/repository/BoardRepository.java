package com.example.mdswebsite.repository;

import com.example.mdswebsite.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByOwnerUserIdOrderByOrderIndex(Long id);
}
